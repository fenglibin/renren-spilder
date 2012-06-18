package it.renren.spilder.util.wash;

import it.renren.spilder.dao.AddonarticleDAO;
import it.renren.spilder.dataobject.AddonarticleDO;
import it.renren.spilder.parser.AHrefElement;
import it.renren.spilder.parser.AHrefParser;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.UrlUtil;

import java.sql.SQLException;
import java.util.List;

import org.htmlparser.util.ParserException;

/**
 * 去掉addonarticle表中body字段的所有script，以及将所有的外链URL都修改为GO URL的形式<br>
 * 类RemoveScriptTags.java的实现描述：TODO 类实现描述
 * 
 * @author Administrator 2012-6-17 上午10:53:53
 */
public class RemoveScriptTagsAddMakeGoUrl extends WashBase {

    private static String tablePrefix = "renren";
    private static int    pageSize    = 100;
    private static String FROM        = "From：";
    private static String charset     = "gbk";

    /**
     * @param args
     * @throws SQLException
     * @throws ParserException
     */
    public static void main(String[] args) throws SQLException, ParserException {
        initArgs(args);
        removeScript();
    }

    private static void removeScript() throws SQLException, ParserException {
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
        String body = null;
        while (start <= total) {
            addonarticleDOList = addonarticleDAO.selectByPagesize(start, getPageSize());
            for (AddonarticleDO addonarticleDO : addonarticleDOList) {
                body = null;
                body = addonarticleDO.getBody();
                if (addonarticleDO.getBody().indexOf("<script") > 0) {
                    body = StringUtil.removeScript(body);
                }
                body = UrlUtil.replaceHref2GoUrl(body, getCharset());
                body = changeFromLinkText(body);
                addonarticleDO.setBody(body);
                addonarticleDAO.updateBodyByAid(addonarticleDO);
            }
            start += getPageSize();
        }
    }

    public static String changeFromLinkText(String content) throws ParserException {
        if (StringUtil.checkExistOnlyOnce(content, FROM)) {
            int index = content.indexOf(FROM);
            String strTemp = content.substring(index);
            List<AHrefElement> childLinks = AHrefParser.ahrefParser(strTemp, null, null, getCharset(), Boolean.FALSE);
            String strTemp2 = strTemp;
            for (AHrefElement href : childLinks) {
                if (href.getHrefText().indexOf("CCCCCC") < 0) {
                    strTemp2 = strTemp2.replace(href.getHrefText() + "<", "<font color=#CCCCCC>Network</font><");
                }
                break;
            }
            content = content.replace(strTemp, strTemp2);
        }
        return content;
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

    private static String getCharset() {
        if (System.getProperty("charset") != null) {
            charset = System.getProperty("charset");
        }
        return charset;
    }

}
