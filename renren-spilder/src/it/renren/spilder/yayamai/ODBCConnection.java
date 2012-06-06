package it.renren.spilder.yayamai;

import it.renren.spilder.util.log.Log4j;

import java.sql.Connection;
import java.sql.DriverManager;

public class ODBCConnection {

    private static Log4j log4j = new Log4j(ODBCConnection.class.getName());

    public static Connection getConnectionByODBC() {
        try {
            String url = "jdbc:odbc:links22-88";
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            Connection conn = DriverManager.getConnection(url, "", "");
            return conn;
        } catch (Exception e) {
            log4j.logError(e);
        }
        return null;
    }
}
