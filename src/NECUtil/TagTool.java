package NECUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.Triple;
import entity.Scholar;


public class TagTool {
	
	private static String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
	
	private static AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
	
	/**
	 * 
	 * @param s is the input string, such as a string extrated from a web page
	 * @return a string tagged with [person��location��organization]
	 */
	public static List<Triple<java.lang.String,java.lang.Integer,java.lang.Integer>> tagStr(String s) {
		//return classifier.classifyWithInlineXML(s);
		return classifier.classifyToCharacterOffsets(s);
	}
		
	public Scholar computResult(String s) {
		Scholar sc = new Scholar();

		List<Triple<java.lang.String,java.lang.Integer,java.lang.Integer>> taggedStr = tagStr(s);
		//System.out.println(taggedStr);
		String scholarname  = (RegexTool.countPerson(s, taggedStr)).toString();
		String scholaremail = (RegexTool.extractEmail(s)).toString();
		String scolarfield  = (RegexTool.extractField(s)).toString();
		sc.setName(scholarname);
		sc.setEmail(scholaremail);
		sc.setField(scolarfield);
		
		return sc;
	}

	public static void main(String[] args) {
		
		String tempstr=new String();
		String str=new String();
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader("data//text1.txt"));
			while((tempstr=in.readLine())!=null){
				 str=str+tempstr+"\n";
			 }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		TagTool tt = new TagTool();
		System.out.println(tt.computResult(str).toString());
	}

}
