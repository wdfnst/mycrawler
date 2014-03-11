package com.googlecode.flaxcrawler.samples;

import NECUtil.CategoryCodeBloomFilter;
import NECUtil.TagTool;

import com.googlecode.flaxcrawler.CrawlerConfiguration;
import com.googlecode.flaxcrawler.CrawlerController;
import com.googlecode.flaxcrawler.CrawlerException;
import com.googlecode.flaxcrawler.DefaultCrawler;
import com.googlecode.flaxcrawler.download.DefaultDownloaderController;
import com.googlecode.flaxcrawler.model.CrawlerTask;
import com.googlecode.flaxcrawler.model.Page;
import com.googlecode.flaxcrawler.parse.DefaultParserController;

import dao.MySQLDao;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import entity.Scholar;
import entity.UniversityPageinfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import synonym.SynonymUtil;
import uk.ac.shef.dcs.oak.jate.JATEException;
import uk.ac.shef.dcs.oak.jate.test.RIDFKeywordExtractor;
import util.Htmltools;
import util.Urltools;

/**
 * Simple sample of flax crawler usage
 */
public class ExtractMainContentCrawler {

	private static MySQLDao mysqldao = new MySQLDao();

//	private static RIDFKeywordExtractor ridfke = new RIDFKeywordExtractor();
	private static TagTool tt = new TagTool();
	
	static {
		try {
			mysqldao.prepareConnection();
			mysqldao.con.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> getSeedlist() throws MalformedURLException{
		List<String> seedlist = new ArrayList<String>();
		MySQLDao csd = null;
		try {
			csd = new MySQLDao();
			csd.prepareConnection();
			csd.con.setAutoCommit(false);
			csd.prepareConnection();
			csd.ps = csd.con.prepareStatement("select url from university");
			ResultSet rs = csd.ps.executeQuery();
			while (rs.next()) {
				String seed = Urltools.formalizeStr2Url(rs.getString("url"));
				System.out.println(seed);
				seedlist.add(seed);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			csd.close();
		}
		return seedlist;
	}
	
    @Test
    public void testStartSimpleSample() throws MalformedURLException, CrawlerException {
        main(null);
    }

    public static void main(String[] args) throws MalformedURLException, CrawlerException {
        // Setting up downloader controller
        DefaultDownloaderController downloaderController = new DefaultDownloaderController();
        // Setting up parser controller
        DefaultParserController parserController = new DefaultParserController();

        // Creating crawler configuration object
        CrawlerConfiguration configuration = new CrawlerConfiguration();

        // Creating five crawlers (to work with 5 threads)
        for (int i = 0; i < 25; i++) {
            // Creating crawler and setting downloader and parser controllers
            DefaultCrawler crawler = new ExampleCrawler();
            crawler.setDownloaderController(downloaderController);
            crawler.setParserController(parserController);
            // Adding crawler to the configuration object
            configuration.addCrawler(crawler);
        }

        // Setting maximum parallel requests to a single site limit
        configuration.setMaxParallelRequests(2);
        // Setting http errors limits. If this limit violated for any
        // site - crawler will stop this site processing
        configuration.setMaxHttpErrors(HttpURLConnection.HTTP_CLIENT_TIMEOUT, 10);
        configuration.setMaxHttpErrors(HttpURLConnection.HTTP_BAD_GATEWAY, 10);
        // Setting period between two requests to a single site (in milliseconds)
        configuration.setPolitenessPeriod(500);

        // Initializing crawler controller
        CrawlerController crawlerController = new CrawlerController(configuration);
        // Adding crawler seed
//        crawlerController.addSeed(new URL("http://en.wikipedia.org/"));
        //crawlerController.addSeed(new URL("http://www.ics.uci.edu/"));
        for (String surl : ExtractMainContentCrawler.getSeedlist())
        	crawlerController.addSeed(new URL(surl));
        // Starting and joining our crawler
        crawlerController.start();
        System.out.println(new Date() + "Waiting for 1000 * 60 * 60 * 24 * 365 seconds");
        crawlerController.join(1000 * 60 * 60 * 24 * 365);
        System.out.println(new Date() + "Stopping crawler");
        crawlerController.stop();
        mysqldao.close();
    }

    /**
     * Custom crawler. Extends {@link DefaultCrawler}.
     */
    private static class ExampleCrawler extends DefaultCrawler {

    	private static int pagecounter = 0;
        /**
         * This method is called after each crawl attempt.
         * Warning - it does not matter if it was unsuccessfull attempt or response was redirected.
         * So you should check response code before handling it.
         * @param crawlerTask
         * @param page
         */
        @Override
        protected void afterCrawl(CrawlerTask crawlerTask, Page page) {
            super.afterCrawl(crawlerTask, page);

            if (page == null) {
                System.out.println(crawlerTask.getUrl() + " violates crawler constraints (content-type or content-length or other)");
            } else if (page.getResponseCode() >= 300 && page.getResponseCode() < 400) {
                // If response is redirected - crawler schedulles new task with new url
                System.out.println("Response was redirected from " + crawlerTask.getUrl());
            } else if (page.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Printing url crawled
//            	System.out.println("=====================================================");
//            	System.out.println("=====================================================");
//            	System.out.println("=====================================================");
                System.out.println(pagecounter++ + " " + crawlerTask.getUrl() + ". Found " + (page.getLinks() != null ? page.getLinks().size() : 0) + " links.");
                String content = page.getContentString();
                // extract emails and names
                Scholar scholar = tt.computResult(content);
                String fields = CategoryCodeBloomFilter.extractField(content);
                
				try {
					content = ArticleExtractor.INSTANCE.getText(content);
				} catch (BoilerpipeProcessingException e) {
					content = Htmltools.htmlRemoveTag(content).
	                		replaceAll("(?m)^\\s*$[\n\r]{1,}", "").trim().replaceAll("\t", " ").replaceAll(" +", " ");
				}
				List<String> kwlist = new ArrayList<String>();
//				if (null != content && !content.equals(""))
//					try {
//						kwlist = ridfke.extract(content);
//					} catch (JATEException | IOException e) {
//						kwlist = new ArrayList<String>();
//					}
				String synsetstr = "";
//				if (null != kwlist && kwlist.size() > 0) {
//					for (String kword : kwlist) {
//						synsetstr += SynonymUtil.getSynonymSet(kword);
//					}
//				}
				
//				System.out.println("Content Len:" + content.length() + " " + crawlerTask.getUrl());
//				System.out.println("Keywords: " + kwlist);
//				System.out.println("Synsetstr: " + synsetstr);
//                System.out.println("Emails：" + scholar.toString());
//                System.out.println("Fields: " + fields);
				
        		UniversityPageinfo universitypageinfo = new UniversityPageinfo();
        		universitypageinfo.setPageurl(crawlerTask.getUrl());
        		universitypageinfo.setPagetitle(page.getTitle());
        		universitypageinfo.setPagecontent(content);
        		universitypageinfo.setEmailAddresses(scholar.getEmail());
        		universitypageinfo.setNames(scholar.getName());
        		universitypageinfo.setFields(fields);
        		universitypageinfo.setContentkeyword(kwlist.toString());
        		universitypageinfo.setKeywordsynonym(synsetstr);
        		mysqldao.addPageinfo(universitypageinfo);
        		scholar = null;
        		fields = null;
        		content = null;
        		kwlist = null;
        		synsetstr =  null;
        		universitypageinfo = null;
            }
        }

        /**
         * You may check if you want to crawl next task
         * @param crawlerTask Task that is going to be crawled if you return {@code true}
         * @param parent parent.getUrl() page contain link to a crawlerTask.getUrl() or redirects to it
         * @return
         */
        @Override
        public boolean shouldCrawl(CrawlerTask crawlerTask, CrawlerTask parent) {
            // Default implementation returns true if crawlerTask.getDomainName() == parent.getDomainName()
        	//System.out.println("--------" + pagecounter++ + "--------" + crawlerTask.getDomain() + "--------" + parent.getDomain());
            return super.shouldCrawl(crawlerTask, parent) || (crawlerTask.getDomain() != null && crawlerTask.getDomain().endsWith(parent.getDomain()));
        }
    }
}
