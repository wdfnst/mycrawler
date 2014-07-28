package dao;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;


public class DataOp {
	
	private final String url = "jdbc:mysql://localhost:3306/jzusdb?user=root&password=One2three";
	public Connection con = null;
	private final String update_interest = "insert into universitypageinfo_fields value(?, ?) on duplicate key "
			+ " update fields = ?";
	PreparedStatement ps1 = null;
	private final String update_interest_uif = "update universitypageinfo set fields=? where id=?";
	PreparedStatement ps2 = null;
    
	public void prepareConnection() {
		try {
			if (con == null || con.isClosed()) {
				con = (Connection) DriverManager.getConnection(url);
			}

			ps1 = con.prepareStatement(update_interest);
			ps2 = con.prepareStatement(update_interest_uif);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error:" + e.getMessage());
		}
	}
    
	public boolean updateInterests(String interests, int id) throws SQLException {
		boolean i = false;
		try {
			con.setAutoCommit(false);
			ps1.setInt(1, id);
			ps1.setString(2, interests);
			ps1.setString(3, interests);
			i = ps1.execute();
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			e.printStackTrace();
		}
		return i;
	}

	public int updateInterests_uif(String interests, int id) {
		int i = 0;
		try {
			con.setAutoCommit(false);
			ps2.setInt(1, id);
			ps2.setString(2, interests);
			i = ps2.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return i;

	}
}
