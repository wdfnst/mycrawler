package uk.ac.shef.dcs.oak.jate.core.feature.indexer;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import uk.ac.shef.dcs.oak.jate.JATEException;
import uk.ac.shef.dcs.oak.jate.core.npextractor.CandidateTermExtractor;
import uk.ac.shef.dcs.oak.jate.model.Corpus;
import uk.ac.shef.dcs.oak.jate.model.Document;

/**
 * Builds a GlobalIndexMem from a corpus
 *
 * @author <a href="mailto:z.zhang@dcs.shef.ac.uk">Ziqi Zhang</a>
 */



public class GlobalIndexBuilderMem implements GlobalIndexBuilder{

	private static Logger _logger = Logger.getLogger(GlobalIndexBuilderMem.class);

	private boolean BUILD_TERM_TO_DOC_MAP = true;
	private boolean BUILD_DOC_TO_TERM_MAP = true;



	public GlobalIndexBuilderMem() {
	}

	/**
	 * creates an instance
	 * @param textunit2DocMap true if should create information of textunit-document binary relation
	 * @param doc2TextunitMap true if should create information of document-textunit binary relation
	 */
	public GlobalIndexBuilderMem(boolean textunit2DocMap, boolean doc2TextunitMap) {
		BUILD_TERM_TO_DOC_MAP = textunit2DocMap;
		BUILD_DOC_TO_TERM_MAP = doc2TextunitMap;
	}

	/**
	 * Build a GlobalIndexMem from a corpus, using the specified text unit extractor
	 * @param c
	 * @param extractor
	 * @return
	 * @throws uk.ac.shef.dcs.oak.jate.JATEException
	 */
	public GlobalIndexMem build(Corpus c, CandidateTermExtractor extractor) throws JATEException {
		GlobalIndexMem _index = new GlobalIndexMem();
		for (Document d : c) {
			_logger.info("For Document " + d);
			Map<String,Set<String>> nps = extractor.extract(d);
			_index.indexTermWithVariant(nps);
			if (BUILD_DOC_TO_TERM_MAP) _index.indexDocWithTermsCanonical(d, nps.keySet());
			if (BUILD_TERM_TO_DOC_MAP) {
				for (String t : nps.keySet()) _index.indexTermCanonicalInDoc(t, d);
			}
		}
		return _index;
	}
	
	/**
	 * Build a GlobalIndexMem from a String, using the specified text unit extractor
	 * @param c
	 * @param extractor
	 * @return
	 * @throws uk.ac.shef.dcs.oak.jate.JATEException
	 
	public GlobalIndexMem buildstr(List<String> c, CandidateTermExtractor extractor) throws JATEException {
		GlobalIndexMem _index = new GlobalIndexMem();
		for (String d : c) {
			_logger.info("For Document " + d);
			Map<String,Set<String>> nps = extractor.extract(d);
			_index.indexTermWithVariant(nps);
			if (BUILD_DOC_TO_TERM_MAP) _index.indexDocWithTermsCanonical(d, nps.keySet());
			if (BUILD_TERM_TO_DOC_MAP) {
				for (String t : nps.keySet()) _index.indexTermCanonicalInDoc(t, d);
			}
		}
		return _index;
	}*/
}

