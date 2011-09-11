package it.renren.spilder.util.wash;

import it.renren.spilder.dao.AddonarticleDAO;
import it.renren.spilder.dao.ArchivesDAO;
import it.renren.spilder.dao.ArctinyDAO;
import it.renren.spilder.dataobject.AddonarticleDO;
import it.renren.spilder.dataobject.ArchivesDO;
import it.renren.spilder.dataobject.ArctinyDO;
import it.renren.spilder.main.Constants;
import it.renren.spilder.util.FontUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

/**
 * ��ָ���ļ�������ID��Χ�ڵ����£�����Ϊ���� ��Simple2Fanti.java��ʵ��������TODO ��ʵ������
 * 
 * @author fenglibin 2011-8-29 ����08:00:48
 */

public class Simple2Fanti extends WashBase {

    /**
     * @param args
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
        AddonarticleDAO addonarticleDAO = (AddonarticleDAO) ctx.getBean("addonarticleDAO");
        DataSource dataSource = (DataSource) ctx.getBean("dataSourceFanti");
        ArctinyDAO arctinyDAOFanti = (ArctinyDAO) ctx.getBean("arctinyDAOFanti");
        ArchivesDAO archivesDAOFanti = (ArchivesDAO) ctx.getBean("archivesDAOFanti");
        AddonarticleDAO addonarticleDAOFanti = (AddonarticleDAO) ctx.getBean("addonarticleDAOFanti");
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        String sql = "select * from fanti_archives where id>=" + startId;
        if (endId > -1) {
            sql += " and id<=" + endId;
        }
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            int typeId = rs.getInt("typeid");

            int tempTypeId = (int) (Math.random() * 1000) + 9999;/* ��ʱID����Ҫ���ڻ�ȡ��ǰ���������ID */
            ArctinyDO arctinyDO = new ArctinyDO();
            arctinyDO.setTypeid(tempTypeId);
            arctinyDAOFanti.insertArctiny(arctinyDO);

            // �����������ID����ѯ����
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

            /* ����������� */
            AddonarticleDO addonarticleDO = addonarticleDAO.selectBodyByAid(arctinyDO.getId());
            String content = addonarticleDO.getBody();
            content = content.replace("/uploads/allimg/", Constants.RenRen_URL + "/uploads/allimg/");
            AddonarticleDO addonarticleDOFanti = new AddonarticleDO();
            addonarticleDOFanti.setAid(arctinyDO.getId());
            addonarticleDOFanti.setTypeid(typeId);
            addonarticleDOFanti.setBody(FontUtil.jian2fan(content));
            addonarticleDAOFanti.insertAddonarticle(addonarticleDOFanti);

        }
    }

}