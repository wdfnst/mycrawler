package synonym;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.dictionary.Dictionary;


public class SynonymUtil {
	public static String propsFile = "src/synonym/DICMemMap_JWNLProperties.xml";
	static {
		try {
			JWNL.initialize(new FileInputStream(propsFile));
		} catch (FileNotFoundException | JWNLException e) {
			e.printStackTrace();
		}
	}
	
	// get dest word's synonyms, it's could work only to nun
	public static List getSynonymSet(String dest) {
		
		Set<String> synwordset = new HashSet<String>();
		long[] so = null;
		try {
			IndexWord  indexword = Dictionary.getInstance().getIndexWord(POS.NOUN, dest);
			if (null != indexword)
				so = indexword.getSynsetOffsets();
			if (null != so)
				for (Long lo : so) {
					Word[] words = Dictionary.getInstance().getSynsetAt(POS.NOUN, lo).getWords();
					for (Word word : words) {
						String lemma = word.getLemma();
						if (!dest.toLowerCase().equals(lemma.toLowerCase()))
							synwordset.add(lemma);
					}
				}
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		
		// only return 10 synonyms
		if (synwordset.size() > 10)
			return Arrays.asList(synwordset.toArray()).subList(0, 10);
		
		return Arrays.asList(synwordset.toArray());
	}
	
	public static void main(String args[]) {
		
		List<String> synlist = SynonymUtil.getSynonymSet("computer");
		System.out.println(synlist);
	}

}
