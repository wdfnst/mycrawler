package com.skjegstad.utils;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.Urltools;
import NECUtil.HTMLSpirit;
import NECUtil.TagTool;
import dao.MySQLDao;

public class KeywordProcessor {
	private static MySQLDao mysqldao = new MySQLDao();

//	private static RIDFKeywordExtractor ridfke = new RIDFKeywordExtractor();
	//private static TagTool tt = new TagTool();
	
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
			csd.ps = csd.con.prepareStatement("select keyword from article");
			ResultSet rs = csd.ps.executeQuery();
			while (rs.next()) {
				String keyword_str = HTMLSpirit.delHTMLTag(rs.getString("keyword"));
				String keywords[] = keyword_str.split("[,;]");
				System.out.println(keyword_str);
				for(String s : keywords)
					System.out.print(s.toString());
				System.out.println("\n");

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			csd.close();
		}
		return seedlist;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			KeywordProcessor.getSeedlist();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
