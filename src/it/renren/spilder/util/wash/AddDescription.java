package it.renren.spilder.util.wash;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.sql.DataSource;

import it.renren.spilder.dao.ArchivesDAO;
import it.renren.spilder.dataobject.ArchivesDO;
import it.renren.spilder.main.Constants;
import it.renren.spilder.util.StringUtil;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 类AddDescription.java的实现描述：补充描述内容，以及删除内容长度小于100的文章
 * 
 * @author fenglibin 2011-6-16 上午09:43:05
 */
public class AddDescription {

    private static final ConfigurableApplicationContext ctx = new FileSystemXmlApplicationContext(
                                                                                                  new String[] { Constants.SPRING_CONFIG_FILE });

    private void removeShotContentSimple() throws SQLException {
        DataSource dataSource = (DataSource) ctx.getBean("dataSource");
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        Statement stDelete = conn.createStatement();
        ResultSet rs = st.executeQuery("select aid from renrenaddonarticle where length(body)<=100");
        while (rs.next()) {
            int aid = rs.getInt("aid");
            stDelete.execute("delete from renrenarctiny where id=" + aid);
            stDelete.execute("delete from renrenarchives where id=" + aid);
        }
        stDelete.execute("delete from renrenaddonarticle where length(body)<=100");
        rs.close();
        stDelete.close();
        conn.close();
    }

    private void removeShotContentFan() throws SQLException {
        DataSource dataSource = (DataSource) ctx.getBean("dataSourceFanti");
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        Statement stDelete = conn.createStatement();
        ResultSet rs = st.executeQuery("select aid from renrenfanti_addonarticle where length(body)<=100");
        while (rs.next()) {
            int aid = rs.getInt("aid");
            stDelete.execute("delete from renrenfanti_arctiny where id=" + aid);
            stDelete.execute("delete from renrenfanti_archives where id=" + aid);
        }
        stDelete.execute("delete from renrenfanti_addonarticle where length(body)<=100");
        rs.close();
        stDelete.close();
        conn.close();
    }

    private void changeSimple() throws SQLException {
        System.out.println("Simple Deal Start." + (new Date()));
        DataSource dataSource = (DataSource) ctx.getBean("dataSource");
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        Statement stBody = conn.createStatement();
        ResultSet rs = st.executeQuery("select id from renrenarchives where description=''");
        String body = "";
        ArchivesDAO archivesDAO = (ArchivesDAO) ctx.getBean("archivesDAO");
        ArchivesDO archivesDO = new ArchivesDO();
        while (rs.next()) {
            int id = rs.getInt("id");
            System.out.println("deal id:" + id + " start. now is:" + (new Date()));
            ResultSet rsbody = stBody.executeQuery("select body from renrenaddonarticle where aid=" + id);
            System.out.println("get body ok:" + (new Date()));
            if (rsbody.next()) {
                body = rsbody.getString("body");
                body = StringUtil.removeHtmlTags(body).trim();
                if (body.length() > Constants.CONTENT_LEAST_LENGTH) {
                    body = body.substring(0, Constants.CONTENT_LEAST_LENGTH);
                }
                System.out.println("begin update:" + (new Date()));
                archivesDO.setId(id);
                archivesDO.setDescription(body);
                archivesDAO.updateDescription(archivesDO);
                System.out.println("finish update:" + (new Date()));
            }
            rsbody.close();
            System.out.println("deal id:" + id + " end." + (new Date()));
        }
        rs.close();
        st.close();
        stBody.close();
        conn.close();
        System.out.println("Simple Deal End." + (new Date()));
    }

    private void changeFan() throws SQLException {
        System.out.println("Fan Deal Start." + (new Date()));
        DataSource dataSource = (DataSource) ctx.getBean("dataSourceFanti");
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        Statement stBody = conn.createStatement();
        ResultSet rs = st.executeQuery("select id from renrenfanti_archives where description=''");
        String body = "";
        ArchivesDAO archivesDAO = (ArchivesDAO) ctx.getBean("archivesDAOFanti");
        ArchivesDO archivesDO = new ArchivesDO();
        while (rs.next()) {
            int id = rs.getInt("id");
            System.out.println("deal id:" + id + " start. now is:" + (new Date()));
            ResultSet rsbody = stBody.executeQuery("select body from renrenfanti_addonarticle where aid=" + id);
            System.out.println("get body ok:" + (new Date()));
            if (rsbody.next()) {
                body = rsbody.getString("body");
                body = StringUtil.removeHtmlTags(body).trim();
                if (body.length() > Constants.CONTENT_LEAST_LENGTH) {
                    body = body.substring(0, Constants.CONTENT_LEAST_LENGTH);
                }
                System.out.println("begin update:" + (new Date()));
                archivesDO.setId(id);
                archivesDO.setDescription(body);
                archivesDAO.updateDescription(archivesDO);
                System.out.println("finish update:" + (new Date()));
            }
            rsbody.close();
            System.out.println("deal id:" + id + " end." + (new Date()));
        }
        rs.close();
        st.close();
        stBody.close();
        conn.close();
        System.out.println("Fan Deal End." + (new Date()));
    }

    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        AddDescription add = new AddDescription();
        // add.removeShotContentSimple();
        // add.removeShotContentFan();
        add.changeSimple();
        add.changeFan();
    }
}
