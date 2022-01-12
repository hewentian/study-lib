package com.hewentian.util;

import java.sql.*;

public final class JdbcUtil {
    private static String url;
    private static String username;
    private static String password;
    private static String driverClassName;

    static {
        url = Config.get("mysql.url", null);
        username = Config.get("mysql.username", null);
        password = Config.get("mysql.password", null);
        driverClassName = Config.get("mysql.driver-class-name", null);

        // password can be null
        if (null == url || null == username || null == driverClassName) {
            String usage = "warn: url, username, driverClassName can not be blank.";
            System.out.println(usage);
            System.exit(1);
        }
    }

    private JdbcUtil() {
    }

    public static Connection getConnection() {
        Connection conn = null;

        try {
            Class.forName(driverClassName); // 指定连接类型
            conn = DriverManager.getConnection(url, username, password); // 获取连接
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }


    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (null != rs && !rs.isClosed()) {
                rs.close();
                rs = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (null != stmt && !stmt.isClosed()) {
                stmt.close();
                stmt = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (null != conn && !conn.isClosed()) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
