package it.renren.spilder.util;

import it.renren.spilder.util.log.Log4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FreshData {

    private static Log4j log4j = new Log4j(FreshData.class.getName());

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Connection conn = getConn();
        try {
            Statement st = conn.createStatement();
            Statement st2 = conn.createStatement();
            ResultSet rs = st.executeQuery("select id from arctiny");
            while (rs.next()) {
                String id = rs.getString("id");
                String sql = "update arctiny set senddate=unix_timestamp(now()),sortrank=unix_timestamp(now()) where id="
                             + id;
                st2.executeUpdate(sql);
                Thread.sleep(1000);
            }
            rs.close();
            st.close();
            st2.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log4j.logError(e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                log4j.logError(e);
            }
        }
    }

    private static Connection getConn() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://184.82.12.132:3306/lele", "fenglibin", "fenglibin");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log4j.logError(e);
        }

        return con;
    }
}
