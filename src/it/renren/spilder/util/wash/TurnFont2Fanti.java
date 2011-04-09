package it.renren.spilder.util.wash;

import it.renren.spilder.util.DBOperator;
import it.renren.spilder.util.FontUtil;
import it.renren.spilder.util.log.Log4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * 将文字全部转换为繁体
 * 
 * @author Administrator
 */
public class TurnFont2Fanti {

    private static Log4j log4j = new Log4j(TurnFont2Fanti.class.getName());

    private static Connection initConnFanti() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://184.82.12.132:3306/renrenfanti?characterEncoding=utf8",
                                              "renrenfantiman", "renrenfantiman");
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
        arctype();
        archives();
        addonarticle();
    }

    private static void arctype() {
        DBOperator dbo = new DBOperator();
        Connection conn = initConnFanti();
        dbo.setConn(conn);
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select id,typename from renrenfanti_arctype");
            String id = "";
            String sql = "";
            while (rs.next()) {
                id = rs.getString("id");
                String typename = rs.getString("typename");
                typename = FontUtil.jian2fan(new StringBuffer(typename));
                try {
                    sql = "update renrenfanti_arctype set typename=? where id=" + id;
                    dbo.setSql(sql);
                    ArrayList data = new ArrayList();
                    data.add(typename);
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

    private static void archives() {
        DBOperator dbo = new DBOperator();
        Connection conn = initConnFanti();
        dbo.setConn(conn);
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select id,title,writer,source,keywords,description from renrenfanti_archives");
            String id = "";
            String sql = "";
            while (rs.next()) {
                id = rs.getString("id");
                String title = rs.getString("title");
                String writer = rs.getString("writer");
                String source = rs.getString("source");
                String keywords = rs.getString("keywords");
                String description = rs.getString("description");

                title = FontUtil.jian2fan(new StringBuffer(title));
                writer = FontUtil.jian2fan(new StringBuffer(writer));
                source = FontUtil.jian2fan(new StringBuffer(source));
                keywords = FontUtil.jian2fan(new StringBuffer(keywords));
                description = FontUtil.jian2fan(new StringBuffer(description));
                try {
                    sql = "update renrenfanti_archives set title=?,writer=?,source=?,keywords=?,description=? where id="
                          + id;
                    dbo.setSql(sql);
                    ArrayList data = new ArrayList();
                    data.add(title);
                    data.add(writer);
                    data.add(source);
                    data.add(keywords);
                    data.add(description);
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

    private static void addonarticle() {
        DBOperator dbo = new DBOperator();
        Connection conn = initConnFanti();
        dbo.setConn(conn);
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select aid,body from renrenfanti_addonarticle");
            String id = "";
            String sql = "";
            while (rs.next()) {
                id = rs.getString("aid");
                String body = rs.getString("body");
                body = FontUtil.jian2fan(new StringBuffer(body));
                try {
                    sql = "update renrenfanti_addonarticle set body=? where aid=" + id;
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
