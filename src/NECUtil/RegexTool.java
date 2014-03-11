package NECUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.util.Triple;

public class RegexTool {
	
	private static Pattern emailPattern = Pattern.compile("(\\w[-.\\w]*\\@[^-][-a-z0-9]+(\\.[-a-z0-9]+)*\\.(com|edu|info))", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);
	private static Pattern personNamePattern =Pattern.compile("(([A-Z]\\.?\\s?)*([A-Z][a-z]+\\.?\\s?)+([A-Z]\\.?\\s?[a-z]*)*)", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);
	
	/**
	 * This function is extract email addr
	 * @param s
	 * s is the input string which contain email addr
	 * @return a list of email addr
	 */
	public static List<String> extractEmail(String s) {
		List<String> emaillist = new ArrayList<String>();
		String find=null;
		Matcher mat=emailPattern.matcher(s);
		while(mat.find()){
			find=mat.group(1);
			emaillist.add(find);
			//System.out.println("Find:"+find);
		}
		//if(find==null)
			  //System.out.print("Can't Find");
		find = null;
		mat = null;
		return emaillist;
	}
	
	/**
	 * 
	 * @param sourceStr
	 * sentences - The string to be classified
	 * @param taggedTripleList
	 * A List of Triples, each of which gives an entity type and the beginning and ending character offsets.
	 * @return
	 */
	public static Map<String, Integer> countPerson(String sourceStr, List<Triple<java.lang.String,java.lang.Integer,java.lang.Integer>> taggedTripleList) {
		Map<String, Integer> personmap = new HashMap<String, Integer>();
		for(Triple<java.lang.String,java.lang.Integer,java.lang.Integer> item : taggedTripleList) {
			if (item.first.equals("PERSON")) {
				String name = sourceStr.substring(item.second, item.third);
				//System.out.println(name);
				name = qualifyPersonName(name);
				if (personmap.containsKey(name)) {
					personmap.put(name, personmap.get(name)+1);
				}else
					personmap.put(name, 1);
			}
		}
		
		return HashMapSort.sortByComparator(personmap, HashMapSort.DESC);
	}
	
	/**
	 * This function is to extract the research field
	 * @param s
	 * @return a list of research field
	 */
	public static List<String> extractField(String s) {
		
		return new ArrayList<String>();
	}
	
	public static String qualifyPersonName(String name) {
		String find = "";
		
		// constrain the length of name less than 100 letters
		if (name.length() > 100) return find;
		
		// prune the name: replace \n \t with "" and replace "  +" with " "
		name = name.replaceAll("\n", " ").replaceAll("\t", " ").replaceAll(" +", " ").trim();
		
		// use a regex to check whether name is satisfying the criteria
		Matcher mat = personNamePattern.matcher(name);
		while(mat.find()){
			find=mat.group(1);
			//System.out.println("Find:"+find);
		}
		return find;
	}
	
	public static void main(String args[]) {
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
		
		System.out.println(extractEmail(str).toString());
		System.out.println((new ArrayList<String>()).toString());
	}
}
