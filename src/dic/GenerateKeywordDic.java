package dic;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import NECUtil.HTMLSpirit;
import cue.lang.WordIterator;
import cue.lang.stop.StopWords;
import dao.MySQLDao;

public class GenerateKeywordDic {

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
	
	public static Set<String> getKeywords() throws MalformedURLException{
		Set<String> keywordlist = new HashSet<String>();//new ArrayList<String>();
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
				for(String s : keywords) {
					for (String word : new WordIterator(s)) {
						word = word.toLowerCase();
						if (!StopWords.English.isStopWord(word)/* && instance.contains(word)*/) 
							keywordlist.add(word);
					}
				}
//				System.out.println("\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			csd.close();
		}
		return keywordlist;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			GenerateKeywordDic.getKeywords();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
