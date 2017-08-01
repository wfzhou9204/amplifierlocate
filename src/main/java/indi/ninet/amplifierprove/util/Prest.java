package indi.ninet.amplifierprove.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author wfzhou modify 2017/5/31 数据库操作工具类
 */
public class Prest {
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private int result = 0;
	private Connection conn;

	/**
	 * 
	 * @param conn
	 *            数据库连接
	 * @param sql
	 *            sql语句
	 * @param value
	 *            动态sql语句中的值
	 * @param type
	 *            value的类型
	 * @param isgetId
	 *            是否插入立即获取编号
	 */
	public Prest(Connection conn, String sql, String value[], int type[],
			boolean isgetId) {
		this.conn = conn;
		try { // 建立连接
			if (isgetId)// 插入立即获得编号
			{
				pstmt = conn.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);
			} else
				pstmt = conn.prepareStatement(sql,
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
			// 没有参数
			if (value == null || value.length == 0 || type == null
					|| type.length == 0)
				return;
			for (int i = 0; i < value.length; i++) {
				switch (type[i]) {
				case 1: {
					if (value[i] != null && !value[i].equals("")
							&& !value[i].equals("null"))
						pstmt.setString(i + 1, value[i]);
					else
						pstmt.setString(i + 1, null);
					break;
				}
				case 2: {

					try {
						if (value[i] != null && !value[i].equals("")
								&& !value[i].equals("null"))
							pstmt.setInt(i + 1, Integer.parseInt(value[i]));
						else
							pstmt.setString(i + 1, null);
					} catch (NumberFormatException nfe) {
						System.out.println("数据格式异常" + 2);
						return;
					}
					break;
				}
				case 3: {

					try {
						if (value[i] != null && !value[i].equals("")
								&& !value[i].equals("null"))
							pstmt.setDouble(i + 1, Double.parseDouble(value[i]));
						else
							pstmt.setString(i + 1, null);
					} catch (NumberFormatException nfe) {
						System.out.println("数据格式异常" + 3);
						return;
					}

					break;
				}
				case 4: {

					try {
						if (value[i] != null && !value[i].equals("")
								&& !value[i].equals("null"))
							pstmt.setFloat(i + 1, Float.parseFloat(value[i]));
						else {
							pstmt.setString(i + 1, null);
						}
					} catch (NumberFormatException nfe) {
						System.out.println("数据格式异常" + 4);
						return;
					}

					break;
				}
				case 5: {

					try {
						if (value[i] != null && !value[i].equals("")
								&& !value[i].equals("null"))
							pstmt.setLong(i + 1, Long.parseLong(value[i]));
						else {
							pstmt.setString(i + 1, null);
						}
					} catch (NumberFormatException nfe) {
						System.out.println("数据格式异常" + 5);
						return;
					}

					break;
				}
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} 
	}

	public ResultSet executeQuery() { // 数据查询
		try {
			rs = pstmt.executeQuery();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} 
		return rs;
	}

	public int executeUpdate() { // 数据更新
		try {
			result = pstmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} 
		return result;
	}

	public PreparedStatement getPreparedStatement() {
		return this.pstmt;
	}

	public void closeDB() { // 断开数据库
		try {
			if (pstmt != null&&!pstmt.isClosed())
				pstmt.close();
			if (conn != null&&!conn.isClosed())
				conn.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
