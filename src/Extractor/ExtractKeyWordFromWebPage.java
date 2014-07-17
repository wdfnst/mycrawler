package Extractor;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import cue.lang.stop.StopWords;
import dao.MySQLDao;
import dic.GenerateKeywordDic;
import NECUtil.HTMLSpirit;
import NECUtil.KeywordBloomFilter;
import NECUtil.StringUtil;

public class ExtractKeyWordFromWebPage {
	
	private static MySQLDao mysqldao = new MySQLDao();

	static {
		try {
			mysqldao.prepareConnection();
			mysqldao.con.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getIntrests() {
		Set<String> keywordlist = new HashSet<String>();//new ArrayList<String>();
		MySQLDao csd = null;
		try {
			csd = new MySQLDao();
			csd.prepareConnection();
			csd.con.setAutoCommit(false);
			csd.prepareConnection();
			csd.ps = csd.con.prepareStatement("select pagecontent, fields from universitypageinfo");
			ResultSet rs = csd.ps.executeQuery();
			while (rs.next()) {
//				System.out.println("+" + rs.getString("fields"));
				Set<String> old_intrests = StringUtil.string2Set(rs.getString("fields"));
				String pageinfo = HTMLSpirit.delHTMLTag(rs.getString("pagecontent"));
				String intrests = KeywordBloomFilter.extractField(pageinfo);
				Set<String> new_intrests = StringUtil.string2Set(intrests);
				new_intrests.addAll(old_intrests);
				System.out.println("+" + new_intrests);
				System.out.println("-" + old_intrests);
				System.out.println("-" + intrests);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			csd.close();
		}
	}
	
	public static void main(String[] args) {
		Set<String> keyword = null;
		ExtractKeyWordFromWebPage ekw = new ExtractKeyWordFromWebPage();
		try {
			keyword = GenerateKeywordDic.getKeywords();
			KeywordBloomFilter.constructBloomFilter(keyword);
			ekw.getIntrests();			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

}
