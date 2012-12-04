package it.renren.spilder.util.wash;

import it.renren.spilder.dao.AddonarticleDAO;
import it.renren.spilder.dao.ArchivesDAO;
import it.renren.spilder.dataobject.AddonarticleDO;
import it.renren.spilder.dataobject.ArchivesDO;
import it.renren.spilder.dataobject.ArctinyDO;
import it.renren.spilder.util.FontUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

/**
 * 将简体中的内容拷贝到繁体中
 * 
 * @author Administrator 2012-7-3 上午07:51:17
 */
public class CopyAndTranslateArticle extends WashBase {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    private static void tran(int startId, int endId) throws SQLException {
        ArchivesDAO archivesDAO = (ArchivesDAO) ctx.getBean("archivesDAO");
        AddonarticleDAO addonarticleDAO = (AddonarticleDAO) ctx.getBean("addonarticleDAO");
        DataSource dataSource = (DataSource) ctx.getBean("dataSource");
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        String sql = "select * from renrenarchives where typeid=115";
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            ArctinyDO arctinyDO = new ArctinyDO();

            ArchivesDO archivesDO = new ArchivesDO();
            archivesDO.setId(rs.getInt("id") + 1000000);
            archivesDO.setTypeid(rs.getInt("typeid"));
            archivesDO.setTitle(jian2fan(rs.getString("title")));
            archivesDO.setKeywords(jian2fan(rs.getString("keywords")));
            archivesDO.setDescription(jian2fan(rs.getString("description")));
            archivesDO.setClick(rs.getInt("click"));
            archivesDO.setWriter(jian2fan(rs.getString("writer")));
            archivesDO.setSource(jian2fan(rs.getString("source")));
            archivesDO.setWeight(rs.getInt("weight"));
            archivesDO.setDutyadmin(1);
            archivesDO.setFlag(rs.getString("flag"));
            archivesDO.setLitpic(jian2fan(rs.getString("litpic")));
            archivesDO.setFilename(rs.getString("filename"));
            archivesDO.setIsmake(rs.getInt("ismake"));
            archivesDO.setPubdate(rs.getInt("pubdate"));
            archivesDO.setSenddate(rs.getInt("senddate"));
            archivesDO.setSortrank(rs.getInt("sortrank"));
            archivesDAO.insertArchives(archivesDO);
        }

        ArchivesDAO archivesDAOFanti = (ArchivesDAO) ctx.getBean("archivesDAOFanti");
        AddonarticleDAO addonarticleDAOFanti = (AddonarticleDAO) ctx.getBean("addonarticleDAOFanti");
        DataSource dataSourceFanti = (DataSource) ctx.getBean("dataSourceFanti");
        Connection connFanti = dataSourceFanti.getConnection();
        Statement stFanti = connFanti.createStatement();
        String sqlFanti = "select * from fanti_archives where id>=" + startId;
        if (endId > -1) {
            sqlFanti += " and id<=" + endId;
        }
        ResultSet rsFanti = stFanti.executeQuery(sqlFanti);
        while (rsFanti.next()) {
            String title = rsFanti.getString("title");
            String keywords = rsFanti.getString("keywords");
            String description = rsFanti.getString("description");
            int id = rsFanti.getInt("id");
            ArchivesDO archivesDO = new ArchivesDO();
            archivesDO.setId(id);
            archivesDO.setTitle(FontUtil.jian2fan(title));
            archivesDO.setKeywords(FontUtil.jian2fan(keywords));
            archivesDO.setDescription(FontUtil.jian2fan(description));
            archivesDAOFanti.updateTitleKeywordsDescription(archivesDO);

            AddonarticleDO addonarticleDO = addonarticleDAOFanti.selectBodyByAid(id);
            addonarticleDO.setAid(id);
            addonarticleDO.setBody(FontUtil.jian2fan(addonarticleDO.getBody()));
            addonarticleDAOFanti.updateBodyByAid(addonarticleDO);
        }
        rsFanti.close();
        stFanti.close();
        connFanti.close();

    }

    private static String jian2fan(String string) {
        try {
            string = FontUtil.jian2fan(new StringBuffer(string));
        } catch (Exception e) {
            string = FontUtil.jian2fan(new StringBuffer(string));
        }
        return string;
    }

}
