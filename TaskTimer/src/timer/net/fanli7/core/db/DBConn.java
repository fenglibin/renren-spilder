package net.fanli7.core.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {

    public static Connection getConn() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        String driver = "com.mysql.jdbc.Driver";
        // URL指向要访问的数据库名scutcs
        String url = "jdbc:mysql://127.0.0.1:3306/fanli7_v8";
        // MySQL配置时的用户名
        String user = "fanli";
        // Java连接MySQL配置时的密码
        String password = "fanli";
        // 加载驱动程序
        Class.forName(driver);
        // 连续数据库
        conn = DriverManager.getConnection(url, user, password);
        return conn;
    }
}
