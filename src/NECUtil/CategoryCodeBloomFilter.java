package NECUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.skjegstad.utils.BloomFilter;

import cue.lang.WordIterator;
import cue.lang.stop.StopWords;
import entity.Scholar;

public class CategoryCodeBloomFilter {
	
	static Random r = new Random();
    private static BloomFilter<String> instance = new BloomFilter<String>(0.0001, 5000);
    static {
    	try {
			constructBloomFilter();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static void constructBloomFilter() throws IOException {
		Set<String> categoryword = new HashSet<String>();
		
		String filename = ConfigureFileUtil.config.getString("classification_file");
		//ClassLoader.getSystemResource(filename).getFile()
		//CategoryCodeBloomFilter.class.getResource(filename).getFile()
		File file=new File("ClassificationInstructional.dic");

		if(!file.exists()||file.isDirectory())
			System.out.println("");
		//BufferedReader br = new BufferedReader(new FileReader(file));
		InputStream inputStream= CategoryCodeBloomFilter.class.getResourceAsStream("ClassificationInstructional.dic");
		Charset encoding = Charset.forName("UTF-8");
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, encoding));
		
		String temp=null;
		temp=br.readLine();
		while((temp = br.readLine())!= null){
			//instance.add(UUID.randomUUID().toString());
			for (String word : new WordIterator(temp)) {
				word = word.toLowerCase();
				if (!StopWords.English.isStopWord(word)) {
					System.out.println("Adding category to Bloom Filter: " + word);
					categoryword.add(word);
				}
			}
		}
		instance.addAll(categoryword);
	}
    
    public static String extractField(String content) {
    	String retstr = "";
    	if (null == content || content.length() < 1)
    		return retstr;
    	
		Set<String> fields = new HashSet<String>();
		for (String word : new WordIterator(content)) {
			word = word.toLowerCase();
			if (!StopWords.English.isStopWord(word) && instance.contains(word)) 
				fields.add(word);
		}
		if (fields.size() > 0)
			retstr = fields.toString();
		fields = null;
		
		return retstr;
    }

	public static void main(String[] args) throws IOException {
		CategoryCodeBloomFilter.constructBloomFilter();

	}

}
