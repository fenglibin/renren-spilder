package it.renren.spilder.util.wash;

import it.renren.spilder.util.DBOperator;
import it.renren.spilder.util.log.Log4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * 作SEO优化 对直接采集来的内容，在其中补充空格或者隐藏的网站信息，让搜索引擎认为这不是采集的文章。
 * 方式为在文章的"</p>"处，补充隐藏的网站内容信息"<div style="display:none">此文来自www.renren.it，请访问<a href="
 * www.renren.it">www.renren.it</a><div>"。
 * 
 * @author Administrator
 */
public class WashContent {

    private static Log4j log4j = new Log4j(WashContent.class.getName());

    private static Connection initConn() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://184.82.12.132:3306/renren?characterEncoding=gbk",
                                              "fenglibin", "fenglibin");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log4j.logError(e);
        }

        return con;
    }

    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        DBOperator dbo = new DBOperator();
        Connection conn = initConn();
        dbo.setConn(conn);
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select aid,body from renrenaddonarticle");
            String id = "";
            String sql = "";
            while (rs.next()) {
                id = rs.getString("aid");
                String body = rs.getString("body");
                body = WashUtil.washData(body,
                                         "<div style=\"display:none\">此文来自www.renren.it，请访问<a href=\"www.renren.it\">www.renren.it</a></div>");
                try {
                    sql = "update renrenaddonarticle set body=? where aid=" + id;
                    dbo.setSql(sql);
                    ArrayList data = new ArrayList();
                    data.add(body);
                    dbo.setBindValues(data);
                    dbo.executeTransactionSql();
                    log4j.logDebug("finished " + id);
                } catch (Exception e) {
                    log4j.logDebug("exception:" + id);
                    /* 保证大部份，小部份就不管了 */
                    log4j.logError(e);
                }
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            log4j.logError(e);
        } finally {
            dbo.closeConn();
            dbo = null;
        }
    }

}
