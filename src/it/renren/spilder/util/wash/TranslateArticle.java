package it.renren.spilder.util.wash;

import it.renren.spilder.dao.AddonarticleDAO;
import it.renren.spilder.dao.ArchivesDAO;
import it.renren.spilder.dataobject.AddonarticleDO;
import it.renren.spilder.dataobject.ArchivesDO;
import it.renren.spilder.util.FontUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

/**
 * 类TranslateArticle.java的实现描述：TODO 将文章ID大于某个值的文章，翻译为繁体
 * 
 * @author fenglibin 2011-8-24 上午10:38:46
 */
public class TranslateArticle extends WashBase {

    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        if (args.length < 1 || args.length > 2) {
            return;
        }
        int startId = Integer.parseInt(args[0]);
        int endId = -1;
        if (args.length == 2) {
            endId = Integer.parseInt(args[1]);
        }
        if (endId > -1 && startId > endId) {
            return;
        }
        tran(startId, endId);

    }

    private static void tran(int startId, int endId) throws SQLException {
        ArchivesDAO archivesDAO = (ArchivesDAO) ctx.getBean("archivesDAOFanti");
        AddonarticleDAO addonarticleDAO = (AddonarticleDAO) ctx.getBean("addonarticleDAOFanti");
        DataSource dataSource = (DataSource) ctx.getBean("dataSourceFanti");
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        String sql = "select * from fanti_archives where id>=" + startId;
        if (endId > -1) {
            sql += " and id<=" + endId;
        }
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            String title = rs.getString("title");
            String keywords = rs.getString("keywords");
            String description = rs.getString("description");
            int id = rs.getInt("id");
            ArchivesDO archivesDO = new ArchivesDO();
            archivesDO.setId(id);
            archivesDO.setTitle(FontUtil.jian2fan(title));
            archivesDO.setKeywords(FontUtil.jian2fan(keywords));
            archivesDO.setDescription(FontUtil.jian2fan(description));
            archivesDAO.updateTitleKeywordsDescription(archivesDO);

            AddonarticleDO addonarticleDO = addonarticleDAO.selectBodyByAid(id);
            if (addonarticleDO != null) {
                addonarticleDO.setAid(id);
                addonarticleDO.setBody(FontUtil.jian2fan(addonarticleDO.getBody()));
                addonarticleDAO.updateBodyByAid(addonarticleDO);
            }
        }
        rs.close();
        st.close();
        conn.close();

    }

}
