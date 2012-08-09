package it.renren.spilder.util.wash;

import it.renren.spilder.dao.AddonarticleDAO;
import it.renren.spilder.dao.ArchivesDAO;
import it.renren.spilder.dao.ArctinyDAO;
import it.renren.spilder.dataobject.AddonarticleDO;
import it.renren.spilder.dataobject.ArchivesDO;
import it.renren.spilder.dataobject.ArctinyDO;
import it.renren.spilder.util.FontUtil;
import it.renren.spilder.util.StringUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

/**
 * 将指定的简体文章ID范围内的文章，翻译为繁体 类Simple2Fanti.java的实现描述：TODO 类实现描述
 * 
 * @author fenglibin 2011-8-29 下午08:00:48
 */

public class Simple2Fanti extends WashBase {

    /**
     * @param args
     */
    public static void main(String[] args) throws SQLException {
        if (args.length < 1 || args.length > 2) {
            return;
        }
        initArgs(args);
        int startId = Integer.parseInt(System.getProperty("startId"));
        int endId = -1;
        if (!StringUtil.isEmpty(System.getProperty("endId"))) {
            endId = Integer.parseInt(System.getProperty("endId"));
        }
        if (endId > -1 && startId > endId) {
            System.out.println("endId:" + endId + " is smaller than startId:" + startId);
            return;
        }
        tran(startId, endId);

    }

    private static void tran(int startId, int endId) throws SQLException {
        AddonarticleDAO addonarticleDAO = (AddonarticleDAO) ctx.getBean("addonarticleDAO");
        DataSource dataSource = (DataSource) ctx.getBean("dataSource");
        ArctinyDAO arctinyDAOFanti = (ArctinyDAO) ctx.getBean("arctinyDAOFanti");
        ArchivesDAO archivesDAOFanti = (ArchivesDAO) ctx.getBean("archivesDAOFanti");
        AddonarticleDAO addonarticleDAOFanti = (AddonarticleDAO) ctx.getBean("addonarticleDAOFanti");
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        String sql = "select * from renrenarchives where id>=" + startId;
        if (endId > -1) {
            sql += " and id<=" + endId;
        }
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            int typeId = rs.getInt("typeid");

            int tempTypeId = (int) (Math.random() * 1000) + 9999;/* 临时ID，主要用于获取当前插入的自增ID */
            ArctinyDO arctinyDO = new ArctinyDO();
            arctinyDO.setTypeid(tempTypeId);
            arctinyDAOFanti.insertArctiny(arctinyDO);

            // 将插入的自增ID给查询出来
            arctinyDO = arctinyDAOFanti.selectArctinyByTypeId(arctinyDO);
            arctinyDO.setTypeid(typeId);
            arctinyDAOFanti.updateArctinyTypeidById(arctinyDO);

            ArchivesDO archivesDO = new ArchivesDO();
            archivesDO.setId(arctinyDO.getId());
            archivesDO.setTypeid(typeId);
            archivesDO.setTitle(FontUtil.jian2fan(rs.getString("title")));
            archivesDO.setKeywords(FontUtil.jian2fan(rs.getString("keywords")));
            archivesDO.setDescription(FontUtil.jian2fan(rs.getString("description")));
            archivesDO.setClick((int) (1000 * Math.random()));
            archivesDO.setWriter(FontUtil.jian2fan(rs.getString("writer")));
            archivesDO.setSource(FontUtil.jian2fan(rs.getString("source")));
            archivesDO.setWeight(arctinyDO.getId());
            archivesDO.setDutyadmin(1);
            archivesDO.setFlag(rs.getString("flag"));
            archivesDO.setLitpic(rs.getString("litpic"));
            archivesDO.setFilename(rs.getString("filename"));
            archivesDAOFanti.insertArchives(archivesDO);

            /* 查出简体内容 */
            AddonarticleDO addonarticleDO = addonarticleDAO.selectBodyByAid(rs.getInt("id"));
            String content = addonarticleDO.getBody();
            AddonarticleDO addonarticleDOFanti = new AddonarticleDO();
            addonarticleDOFanti.setAid(arctinyDO.getId());
            addonarticleDOFanti.setTypeid(typeId);
            addonarticleDOFanti.setBody(FontUtil.jian2fan(content));
            addonarticleDAOFanti.insertAddonarticle(addonarticleDOFanti);

        }
    }

}
