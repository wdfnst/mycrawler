package Extractor;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import dao.DataOp;
import dic.GenerateKeywordDic;
import NECUtil.HTMLSpirit;
import NECUtil.KeywordBloomFilter;
import NECUtil.StringUtil;

public class ExtractKeyWordFromWebPage {
	
	/*
	 * Extract the interests in keyword and integrate with the categories
	 */
	public void getIntrests() {
		String url = "jdbc:mysql://localhost:3306/jzusdb?user=root&password=One2three";
	    try {
	      Class.forName("com.mysql.jdbc.Driver");
	    } catch (ClassNotFoundException e1) {
	      e1.printStackTrace();
	    }
	    long allStart = System.currentTimeMillis();
	    long count =1;

	    Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    DataOp dop = new DataOp();
	    dop.prepareConnection();
	    try {
    		con = DriverManager.getConnection(url);
    		ps = (PreparedStatement) con.prepareStatement("select id, pagecontent, fields from universitypageinfo",
					ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    		ps.setFetchSize(Integer.MIN_VALUE);
    		ps.setFetchDirection(ResultSet.FETCH_REVERSE);
    		rs = ps.executeQuery();
    		while (rs.next()) {
    			int id = Integer.parseInt(rs.getString("id"));
    			Set<String> old_intrests = StringUtil.string2Set(rs.getString("fields"));
				String pageinfo = HTMLSpirit.delHTMLTag(rs.getString("pagecontent"));
				String intrests = KeywordBloomFilter.extractField(pageinfo);
				Set<String> new_intrests = StringUtil.string2Set(intrests);
				new_intrests.addAll(old_intrests);
				dop.updateInterests(new_intrests.toString(), id);
				
	  	        if(count%100==0){
  	        		long end = System.currentTimeMillis();
  	        		System.out.println(" 已提取fields,第  "+(count)+" 条记录，耗时" + ((end - allStart)/1000/60) + " 分 " + 
       					 ((end - allStart)/1000) % 60 + "秒");
	  	        }
				pageinfo = null;
				intrests = null;
				old_intrests = null;
				new_intrests = null;
	  	        count++;
	  	      }
	  	      System.out.println("取回数据量为  "+count+" 行！");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void updateIntrests() {
		String url = "jdbc:mysql://localhost:3306/jzusdb?user=root&password=One2three";
	    try {
	      Class.forName("com.mysql.jdbc.Driver");
	    } catch (ClassNotFoundException e1) {
	      e1.printStackTrace();
	    }
	    long allStart = System.currentTimeMillis();
	    long count =1;

	    Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    DataOp dop = new DataOp();
	    dop.prepareConnection();
	    try {
    		con = DriverManager.getConnection(url);
    		ps = (PreparedStatement) con.prepareStatement("select id, fields from universitypageinfo_fields",
					ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    		ps.setFetchSize(Integer.MIN_VALUE);
    		ps.setFetchDirection(ResultSet.FETCH_REVERSE);
    		rs = ps.executeQuery();
    		while (rs.next()) {
				dop.updateInterests_uif(rs.getString("fields"), Integer.parseInt(rs.getString("id")));
				
	  	        if(count%100==0){
  	        		long end = System.currentTimeMillis();
        			System.out.println(" 已更新fields,第  "+(count)+" 条记录，耗时" + ((end - allStart)/1000/60) + " 分 " + 
        					 ((end - allStart)/1000) % 60 + "秒");
	  	        }
	  	        count++;
	  	      }
	  	      System.out.println("取回数据量为  "+count+" 行！");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void main(String[] args) {
		Set<String> keyword = null;
		ExtractKeyWordFromWebPage ekw = new ExtractKeyWordFromWebPage();
		try {
			keyword = GenerateKeywordDic.getKeywords();
			KeywordBloomFilter.constructBloomFilter(keyword);
			keyword = null;
			ekw.getIntrests();
			ekw.updateIntrests();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
