package it.renren.spilder.util.wash;

import it.renren.spilder.util.DBOperator;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.log.Log4j;

import java.sql.*;
import java.util.ArrayList;

/**
 * 清洗原来获取的数据 如果原来的文章没有关键字，将标题设为关键字；如果原来的内容没有描述，将文章的前100个字符作为描述
 * 
 * @author Administrator
 */
public class WashBeforeContent {

    private static Log4j log4j = new Log4j(WashBeforeContent.class.getName());

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
        // TODO Auto-generated method stub
        int times = 120;
        int startTime = 0;
        if (args.length < 1) {
            startTime = 8;
        } else {
            startTime = Integer.parseInt(args[0]);
        }
        if (args.length == 2) {
            times = Integer.parseInt(args[1]);
        }
        DBOperator dbo = new DBOperator();
        Connection conn = initConn();
        dbo.setConn(conn);
        int time = 0;
        try {
            for (time = startTime; time < times; time++) {
                int start = time * 100;
                int end = (time + 1) * 100;
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("select a.id,substring(b.body,1000) as body from renrenarchives a,renrenaddonarticle b where a.id=b.aid and a.keywords='' and a.id>"
                                               + start + " and a.id<=" + end);
                String id = "";
                String sql = "";
                while (rs.next()) {
                    id = rs.getString("id");
                    String body = rs.getString("body");
                    body = StringUtil.removeHtmlTags(body);
                    body = body.replace("&nbsp;", "");

                    if (body.length() > 100) {
                        body = body.substring(0, 100);
                    }
                    try {
                        sql = "update renrenarchives set keywords=title,description=? where id=" + id;
                        dbo.setSql(sql);
                        ArrayList data = new ArrayList();
                        data.add(body);
                        dbo.setBindValues(data);
                        dbo.executeTransactionSql();
                    } catch (Exception e) {
                        /* 保证大部份，小部份就不管了 */
                        log4j.logError(e);
                    }
                }
                rs.close();
                st.close();
            }
        } catch (Exception e) {
            System.out.print("Time:" + time);
            log4j.logError(e);
        } finally {
            dbo.closeConn();
            dbo = null;
        }
    }

}
