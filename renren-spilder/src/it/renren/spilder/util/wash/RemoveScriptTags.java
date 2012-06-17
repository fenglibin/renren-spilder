package it.renren.spilder.util.wash;

import it.renren.spilder.dao.AddonarticleDAO;
import it.renren.spilder.dataobject.AddonarticleDO;
import it.renren.spilder.util.StringUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * 去掉addonarticle表中body字段的所有script<br>
 * 类RemoveScriptTags.java的实现描述：TODO 类实现描述
 * 
 * @author Administrator 2012-6-17 上午10:53:53
 */
public class RemoveScriptTags extends WashBase {

    private static String tablePrefix = "renren";
    private static int    pageSize    = 100;

    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        initArgs(args);
        removeScript();
    }

    private static void removeScript() throws SQLException {
        AddonarticleDAO addonarticleDAO = null;
        if (getTablePrefix().startsWith("renren")) {
            addonarticleDAO = (AddonarticleDAO) ctx.getBean("addonarticleDAO");
        } else if (getTablePrefix().startsWith("fanti")) {
            addonarticleDAO = (AddonarticleDAO) ctx.getBean("addonarticleDAOFanti");
        }
        List<AddonarticleDO> addonarticleDOList = null;

        int total = addonarticleDAO.getTotalRecords();
        if (total <= 0) {
            throw new RuntimeException("Total number is 0.");
        }
        int start = getStart();
        while (start <= total) {
            addonarticleDOList = addonarticleDAO.selectByPagesize(start, getPageSize());
            for (AddonarticleDO addonarticleDO : addonarticleDOList) {
                addonarticleDO.setBody(StringUtil.removeHtmlTags(addonarticleDO.getBody()));
                addonarticleDAO.updateBodyByAid(addonarticleDO);
            }
            start += getPageSize();
        }
    }

    private static String getTablePrefix() {
        if (System.getProperty("tablePrefix") != null) {
            tablePrefix = System.getProperty("tablePrefix");
        }
        return tablePrefix;
    }

    private static int getStart() {
        if (System.getProperty("start") != null) {
            return Integer.parseInt(System.getProperty("start"));
        }
        return 0;
    }

    private static int getPageSize() {
        if (System.getProperty("pageSize") != null) {
            pageSize = Integer.parseInt(System.getProperty("pageSize"));
        }
        return pageSize;
    }

}
