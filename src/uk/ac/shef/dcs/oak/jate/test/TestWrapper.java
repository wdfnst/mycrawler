package uk.ac.shef.dcs.oak.jate.test;
import java.io.IOException;
import java.util.List;

import uk.ac.shef.dcs.oak.jate.JATEException;
import uk.ac.shef.dcs.oak.jate.test.RIDFKeywordExtractor;


public class TestWrapper {

	public static void main(String[] args) throws JATEException, IOException {
		String extr_str = "The human ability to think abstractly may be unparalleled in the animal kingdom. Humans are one of" + 
				"only six species to pass the mirror test — which tests whether an animal recognizes its" + 
				"reflection as an image of itself — along with chimpanzees, orangutans, dolphins, and possibly" + 
				"pigeons. In October 2006, three elephants at the Bronx Zoo also passed this test.[42] Humans under" + 
				"the age of 2 typically fail this test.[43] However, this may be a matter of degree rather than a" + 
				"sharp divide. Monkeys have been trained to apply abstract rules in tasks.[44]";
		
		RIDFKeywordExtractor ridfke = new RIDFKeywordExtractor();
		List<String> kwlist = ridfke.extract(extr_str);

		System.out.println(kwlist.toString());
	}

}
