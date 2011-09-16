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

/**
 * ��AddDescription.java��ʵ�������������������ݣ��Լ�ɾ�����ݳ���С��100������
 * 
 * @author fenglibin 2011-6-16 ����09:43:05
 */
public class AddDescription extends WashBase {

    // ɾ������С��100�ֽڵ���������
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

    // ɾ������С��100�ֽڵ���������
    private void removeShotContentFan() throws SQLException {
        DataSource dataSource = (DataSource) ctx.getBean("dataSourceFanti");
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        Statement stDelete = conn.createStatement();
        ResultSet rs = st.executeQuery("select aid from fanti_addonarticle where length(body)<=100");
        while (rs.next()) {
            int aid = rs.getInt("aid");
            stDelete.execute("delete from fanti_arctiny where id=" + aid);
            stDelete.execute("delete from fanti_archives where id=" + aid);
        }
        stDelete.execute("delete from fanti_addonarticle where length(body)<=100");
        rs.close();
        stDelete.close();
        conn.close();
    }

    // ��������Ϊ�յ�����
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

    // �����������µ�����
    private void changeSimpleAll() throws SQLException {
        System.out.println("Simple Deal Start." + (new Date()));
        DataSource dataSource = (DataSource) ctx.getBean("dataSource");
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        Statement stBody = conn.createStatement();
        ResultSet rs = st.executeQuery("select id from renrenarchives");
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

    // ��������Ϊ�յ�����
    private void changeFan() throws SQLException {
        System.out.println("Fan Deal Start." + (new Date()));
        DataSource dataSource = (DataSource) ctx.getBean("dataSourceFanti");
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        Statement stBody = conn.createStatement();
        ResultSet rs = st.executeQuery("select id from fanti_archives where description=''");
        String body = "";
        ArchivesDAO archivesDAO = (ArchivesDAO) ctx.getBean("archivesDAOFanti");
        ArchivesDO archivesDO = new ArchivesDO();
        while (rs.next()) {
            int id = rs.getInt("id");
            System.out.println("deal id:" + id + " start. now is:" + (new Date()));
            ResultSet rsbody = stBody.executeQuery("select body from fanti_addonarticle where aid=" + id);
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

    // �����������µ�����
    private void changeFanAll() throws SQLException {
        System.out.println("Fan Deal Start." + (new Date()));
        DataSource dataSource = (DataSource) ctx.getBean("dataSourceFanti");
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        Statement stBody = conn.createStatement();
        ResultSet rs = st.executeQuery("select id from fanti_archives");
        String body = "";
        ArchivesDAO archivesDAO = (ArchivesDAO) ctx.getBean("archivesDAOFanti");
        ArchivesDO archivesDO = new ArchivesDO();
        while (rs.next()) {
            int id = rs.getInt("id");
            System.out.println("deal id:" + id + " start. now is:" + (new Date()));
            ResultSet rsbody = stBody.executeQuery("select body from fanti_addonarticle where aid=" + id);
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

    // ��FAN���е�'���ā�������IT�W��Ո�L��www.renren.it�@ȡ��������'�滻��
    public void replaceContent() throws SQLException {
        System.out.println("Fan Deal Start." + (new Date()));
        DataSource dataSource = (DataSource) ctx.getBean("dataSourceFanti");
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        st.execute("update fanti_addonarticle set body=replace(body,'���ā�������IT�W��Ո�L��www.renren.it�@ȡ��������','')");
        st.close();
        conn.close();
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
//        add.changeSimpleAll();
//        add.changeFanAll();
        // add.replaceContent();
    }
}
