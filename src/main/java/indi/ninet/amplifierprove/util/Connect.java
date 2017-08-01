package indi.ninet.amplifierprove.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
	/**
	 * 作为静态参数,如果配置文件改变了,无法实现动态调整
	 */
	/*
	 * private static final String url = SystemConfigure
	 * .getParValue("database_url"); private static final String drive =
	 * SystemConfigure .getParValue("database_drive"); private static final
	 * String username = SystemConfigure .getParValue("database_username");
	 * private static final String password = SystemConfigure
	 * .getParValue("database_password");
	 */
	private static Connection conn;

	private Connect() {

	}

	public static Connection getConnect() {
		String url = SystemConfigure.getParValue("database_url");
		String drive = SystemConfigure.getParValue("database_drive");
		String username = SystemConfigure.getParValue("database_username");
		String password = SystemConfigure.getParValue("database_password");
		try {
			Class.forName(drive);
			conn = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			System.err.println("无法加载驱动类:" + drive);
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.println("数据库连接异常");
			e.printStackTrace();
		}
		return conn;
	}
}