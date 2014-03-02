package dao;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

import entity.UniversityPageinfo;
import entity.University;

public class MySQLDao {
	
	static {
		// ���������
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("�����ʧ��");
		}
	}
	// ��ݿ������ַ�
	private String url = "jdbc:mysql://localhost:3306/jzusdb";
	// �û���
	private String userName = "jzus";
	// ����
	private String passWord = "mzxwswws";
	// ���Ӷ���
	public Connection con = null;
	// ������
	public PreparedStatement ps = null;
	private String insert_uni_sql = "INSERT IGNORE INTO `jzusdb`.`university` (`id`, `name`, `url`, `location`, `Region`, `mapurl`, `worldrank`, `intro`, `Overallscore`, `Teaching`, `Internationaloutlook`, `Industryincome`, `Research`, `Citations`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	private String insert_pageinfo_sql = "INSERT IGNORE INTO  `jzusdb`.`universitypageinfo` (" + 
										"`id` ," + 
										"`urlencoding` ," + 
										"`pageurl` ," + 
										"`pagetitle` ," + 
										"`emailaddresses` ," + 
										"`names` ," + 
										"`pagecontent` ," + 
										"`fields` ," + 
										"`contentkeyword` ," + 
										"`keywordsynonym`) VALUES ( NULL, MD5(?), ?, ?, ?, ?, ?, ?, ?, ?);";
	
	// ��ݿ����ӷ���
	public void prepareConnection() {
		try {
			if (con == null || con.isClosed()) {
				con = (Connection) DriverManager.getConnection(url, userName, passWord);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("�����쳣:" + e.getMessage());
		}
	}

	// �رշ���
	public void close() {
		try {
			if (ps != null) {
				ps.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("�ر������쳣:" + e.getMessage());
		}
	}

	// �����ع�
	public void rollback() {
		try {
			if (con != null) {
				con.rollback();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("�ع�ʧ��:" + e.getMessage());
		}
	}
	
	public int addUniversity(University uni) {
		// this.user = user;
		int i = 0;
		try {
			prepareConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(insert_uni_sql);
			ps.setString(1, uni.getName());
			ps.setString(2, uni.getUrl());
			ps.setString(3, uni.getLocation());
			ps.setString(4, uni.getRegion());
			ps.setString(5, uni.getMapurl());
			ps.setString(6, uni.getWorldrank());
			ps.setString(7, uni.getIntro());
			ps.setFloat(8, uni.getOverallscore());
			ps.setFloat(9, uni.getTeaching());
			ps.setFloat(10, uni.getInternationaloutlook());
			ps.setFloat(11, uni.getIndustryincome());
			ps.setFloat(12, uni.getResearch());
			ps.setFloat(13, uni.getCitations());
			i = ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			rollback();
			e.printStackTrace();
		} finally {
			close();
		}

		return i;
	}
	
	public int addPageinfo(UniversityPageinfo page) {
		// this.user = user;
		int retryCount = 0;
		int i = 0;
		try {
			prepareConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(insert_pageinfo_sql);
			ps.setString(1, page.getPageurl());
			ps.setString(2, page.getPageurl());
			ps.setString(3, page.getPagetitle());
			ps.setString(4, page.getEmailAddresses());
			ps.setString(5, page.getNames());
			ps.setString(6, page.getPagecontent());
			ps.setString(7, page.getFields());
			ps.setString(8, page.getContentkeyword());
			ps.setString(9, page.getKeywordsynonym());
			
			i = ps.executeUpdate();
			con.commit();
		} catch (SQLException sqlEx) {
			String sqlState = sqlEx.getSQLState();
            if ("08S01".equals(sqlState) || "40001".equals(sqlState)) 
            {                
                retryCount--;            
             } else {                
            	 retryCount = 0;            
             }
		} finally {
			close();
		}
		
		return i;
	}
	
	public static void main(String[] args) throws SQLException {
		MySQLDao csd = new MySQLDao();
		csd.prepareConnection();
		csd.con.setAutoCommit(false);
		
		String sql = "select DOI, subject from jzusdb.article limit 0, 5000 ";
		try {
			csd.ps = csd.con.prepareStatement(sql);
			ResultSet rs = csd.ps.executeQuery();
//			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				System.out.println(rs.getString("DOI") + " "
						+ rs.getString("subject"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
