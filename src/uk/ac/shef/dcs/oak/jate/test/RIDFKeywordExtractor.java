package uk.ac.shef.dcs.oak.jate.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import uk.ac.shef.dcs.oak.jate.JATEException;
import uk.ac.shef.dcs.oak.jate.Utitls.DeleteDirectory;
import uk.ac.shef.dcs.oak.jate.Utitls.FileUtils;
import uk.ac.shef.dcs.oak.jate.Utitls.PropertiesTool;
import uk.ac.shef.dcs.oak.jate.core.algorithm.AbstractFeatureWrapper;
import uk.ac.shef.dcs.oak.jate.core.algorithm.Algorithm;
import uk.ac.shef.dcs.oak.jate.core.algorithm.RIDFAlgorithm;
import uk.ac.shef.dcs.oak.jate.core.algorithm.RIDFFeatureWrapper;
import uk.ac.shef.dcs.oak.jate.core.feature.FeatureBuilderCorpusTermFrequency;
import uk.ac.shef.dcs.oak.jate.core.feature.FeatureCorpusTermFrequency;
import uk.ac.shef.dcs.oak.jate.core.feature.indexer.GlobalIndexBuilderMem;
import uk.ac.shef.dcs.oak.jate.core.feature.indexer.GlobalIndexMem;
import uk.ac.shef.dcs.oak.jate.core.npextractor.CandidateTermExtractor;
import uk.ac.shef.dcs.oak.jate.core.npextractor.WordExtractor;
import uk.ac.shef.dcs.oak.jate.io.ResultWriter2File;
import uk.ac.shef.dcs.oak.jate.model.CorpusImpl;
import uk.ac.shef.dcs.oak.jate.model.Term;
import uk.ac.shef.dcs.oak.jate.util.control.Lemmatizer;
import uk.ac.shef.dcs.oak.jate.util.control.StopList;
import uk.ac.shef.dcs.oak.jate.util.counter.TermFreqCounter;
import uk.ac.shef.dcs.oak.jate.util.counter.WordCounter;

/**
 */
public class RIDFKeywordExtractor {
    private Map<Algorithm, AbstractFeatureWrapper> _algregistry = new HashMap<Algorithm, AbstractFeatureWrapper>();
	private static Logger _logger = Logger.getLogger(AlgorithmTester.class);
	private static StopList stop = null;
	private static String corpus_path = PropertiesTool.getInstance().getProperty("corpuspath");
	static {
		try {
			stop = new StopList(true);
		} catch (IOException e) {
			_logger.error(e);
		}
	}

	public void registerAlgorithm(Algorithm a, AbstractFeatureWrapper f) {
		_algregistry.put(a, f);
	}

	public void execute(GlobalIndexMem index) throws JATEException, IOException {
		_logger.info("Initializing outputter, loading NP mappings...");
		ResultWriter2File writer = new ResultWriter2File(index);
		if (_algregistry.size() == 0) throw new JATEException("No algorithm registered!");
		_logger.info("Running NP recognition...");

		/*.extractNP(c);*/
		for (Map.Entry<Algorithm, AbstractFeatureWrapper> en : _algregistry.entrySet()) {
			_logger.info("Running feature store builder and ATR..." + en.getKey().toString());
			Term[] result = en.getKey().execute(en.getValue());
			writer.output(result, en.getKey().toString() + ".txt");
		}
	}
	
	public List<String> extract(String sourcecontent) throws JATEException, IOException {
		// to support multi-thread, add UUID
		if (null == sourcecontent || sourcecontent.equals(""))
			throw new IOException();
		
		UUID uuid  =  UUID.randomUUID(); 
		String uuiddir = corpus_path + UUID.randomUUID().toString();
		String filepath = FileUtils.createOrRepalceFileContent(uuiddir, sourcecontent);
		
		// instance counter tools
		Lemmatizer lemmatizer = new Lemmatizer();
		CandidateTermExtractor npextractor = new WordExtractor(stop, lemmatizer, true,2);
		TermFreqCounter npcounter = new TermFreqCounter();
		WordCounter wordcounter = new WordCounter();

		// build index
		GlobalIndexBuilderMem builder = new GlobalIndexBuilderMem();
		GlobalIndexMem termDocIndex = builder.build(new CorpusImpl(uuiddir), npextractor);
		FeatureCorpusTermFrequency termCorpusFreq =
				new FeatureBuilderCorpusTermFrequency(npcounter, wordcounter, lemmatizer).build(termDocIndex);

		// Regist Algorithmand and index, and execute
		AlgorithmTester tester = new AlgorithmTester();
		tester.registerAlgorithm(new RIDFAlgorithm(), new RIDFFeatureWrapper(termCorpusFreq));
		Term[] termlist = tester.execute(termDocIndex);
		List<String> kw_list = new ArrayList<String>();
		int counter = 0;
		for (Term tt : termlist) {
			if(!isNumeric(tt.getConcept()) && counter++ < 10)
				kw_list.add(tt.getConcept());
		}
		
		// clear works
		DeleteDirectory.deleteDir(new File(uuiddir));
		lemmatizer = null;
		wordcounter = null;
		npcounter = null;
		builder = null;
		termCorpusFreq = null;
		tester = null;
		termlist = null;
		
		return kw_list;
	}
	
	public static void main(String[] args) throws IOException, JATEException {
		
		String corpuspath = "src/test/example/";
		String outputdir = "src/test/output/";

		String extr_str = "The human ability to think abstractly may be unparalleled in the animal kingdom. Humans are one of" + 
				"only six species to pass the mirror test — which tests whether an animal recognizes its" + 
				"reflection as an image of itself — along with chimpanzees, orangutans, dolphins, and possibly" + 
				"pigeons. In October 2006, three elephants at the Bronx Zoo also passed this test.[42] Humans under" + 
				"the age of 2 typically fail this test.[43] However, this may be a matter of degree rather than a" + 
				"sharp divide. Monkeys have been trained to apply abstract rules in tasks.[44]";
		FileUtils.createOrRepalceFileContent(corpuspath + "temp.text", extr_str);
		
		System.out.println("Started "+ RIDFKeywordExtractor.class+"at: " + new Date() + "... For detailed progress see log file jate.log.");
		
//		StopList stop = new StopList(true);
		Lemmatizer lemmatizer = new Lemmatizer();
		CandidateTermExtractor npextractor = new WordExtractor(stop, lemmatizer, true,2);
		TermFreqCounter npcounter = new TermFreqCounter();
		WordCounter wordcounter = new WordCounter();

		GlobalIndexBuilderMem builder = new GlobalIndexBuilderMem();
		GlobalIndexMem termDocIndex = builder.build(new CorpusImpl(corpuspath), npextractor);
		FeatureCorpusTermFrequency termCorpusFreq =
				new FeatureBuilderCorpusTermFrequency(npcounter, wordcounter, lemmatizer).build(termDocIndex);

		AlgorithmTester tester = new AlgorithmTester();
		tester.registerAlgorithm(new RIDFAlgorithm(), new RIDFFeatureWrapper(termCorpusFreq));
		tester.execute(termDocIndex, outputdir);
		System.out.println("Ended at: " + new Date());
	}
	
	public static boolean isNumeric(String str){
		for (int i = str.length();--i>=0;){   
			if (!Character.isDigit(str.charAt(i))){
				return false;
			}
		}
		return true;
	}
}
