package NECUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Preprocess some string to satify the extracting demands
 * @author zwd
 *
 */
public class TextPreprocess {
	
	
	/**
	 * sift odd email addr format to normal formal, such as:
	 * 1. welling [at] ics [dot] uci [dot] edu
	 * 2. hugh [@ comp.nus.edu.sg] 
	 * 3. ... 
	 * @param email
	 * @return
	 */
	public String sift2NormalEmail(String email) {
		
		// repalce all the odd characters
		email = email.replaceAll("[]", "");
		email = email.replaceAll("at", "@");
		return null;
	}
	
	/**
	 * Preprocess input string include the following:
	 * 1. sift the unformat email addr to abc@edf.efg
	 * 2. ...
	 * @param input
	 * @return
	 */
	public String textPreprocess(String input) {
		
		// repalce all the odd characters [\\p{P}]
		input = input.replaceAll("[^\\w\\s\\.\\@-]", " ");
		// replace the unnecessary space
		input = input.replaceAll(" +", " ").trim();
		// change 'at' to '@', replace space around '@' with ""
		input = input.replaceAll("at", "@");
		input = input.replaceAll("\\s+@\\s+", "@");
		// change 'dot' to '.', replace space around '.' with ""
		input = input.replaceAll("dot", ".");
		input = input.replaceAll("\\s+\\.\\s+", ".");
		
		// find pattern like ".* at .{1, 50}[topdomain]"
		
		// replace all the space around at 
		//input = input.replaceAll("at", "@");
		
		return input;
	}
	
	public static void main(String args[]) {
		
		String testStr1 = "welling [at]    ics [dot] uci [dot] edu     ";
		String testStr2 = "hugh [@ comp.nus.edu.sg]";
		String testStr3 = "";
		String testStr4 = "";
		String testStr5 = "";
		String testStr6 = "";
		
		TextPreprocess tpp = new TextPreprocess();
		System.out.println(tpp.textPreprocess(testStr1));
		System.out.println(tpp.textPreprocess(testStr2));
		System.out.println(tpp.textPreprocess(testStr3));
		System.out.println(tpp.textPreprocess(testStr4));
		System.out.println(tpp.textPreprocess(testStr5));
		System.out.println(tpp.textPreprocess(testStr6));
		
		
//		System.out.println(AdditionInfo.topDomainlist.toString());
		
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
		
		str = tpp.textPreprocess(str);
		System.out.println(str);
		
		Pattern emailPattern = Pattern.compile("(\\w[-.\\w]*\\@[^-][-a-z0-9]+(\\.[-a-z0-9]+)*\\.(com|edu|info))", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);
//		List<String> emaillist = new ArrayList<String>();
		String find=null;
		Matcher mat=emailPattern.matcher(str);
		while(mat.find()){
			find=mat.group(1);
//			emaillist.add(find);
			System.out.println("Find:"+find);
		}
	}
}
