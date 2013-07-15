package it.renren.spilder.util.wash;

import it.renren.spilder.dao.AddonarticleDAO;
import it.renren.spilder.dataobject.AddonarticleDO;
import it.renren.spilder.parser.AHrefElement;
import it.renren.spilder.parser.AHrefParser;
import it.renren.spilder.util.FileUtil;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.UrlUtil;
import it.renren.spilder.util.log.Log4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.util.ParserException;

/**
 * 去掉addonarticle表中body字段的所有script，以及将所有的外链URL都修改为GO URL的形式<br>
 * 类RemoveScriptTags.java的实现描述：TODO 类实现描述
 * 
 * @author Administrator 2012-6-17 上午10:53:53
 */
public class ReadContentFromHtml2DB extends WashBase {

    private static Log4j           log4j              = new Log4j(ReadContentFromHtml2DB.class.getName());

    private static String          tablePrefix        = "renren";
    private static String          FROM               = "From：";
    private static String          charset            = "gbk";
    private static String          basePath           = "/home/fenglibin/www/www.renren.it/a";

    // 匹配文章名
    private static Pattern         articleNamePattern = Pattern.compile("^[0-9]*\\.html");
    private static AddonarticleDAO addonarticleDAO    = null;

    private static boolean         isCheckFile        = Boolean.FALSE;
    private static List<String>    startList          = new ArrayList<String>();
    private static List<String>    endList            = new ArrayList<String>();

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        startList.add("document.getElementById(\"contentGoogleAd\").className=className;");
        startList.add("document.getElementById(\"contentGoogleAd\").className=className;");
        startList.add("<div class=\"content\">");
        endList.add("<div id=\"downContentArea1\"></div>");
        endList.add("<div id=\"downContentGoogleAd\">");
        endList.add("</div><!-- /content -->");

        if (getTablePrefix().startsWith("renren")) {
            addonarticleDAO = (AddonarticleDAO) ctx.getBean("addonarticleDAO");
        } else if (getTablePrefix().startsWith("fanti")) {
            addonarticleDAO = (AddonarticleDAO) ctx.getBean("addonarticleDAOFanti");
        }
        initArgs(args);
        try {
            readContent2DB(getBasePath());
        } finally {
            release();
        }

    }

    private static String changeFromLinkText(String content) throws ParserException {
        if (StringUtil.checkExistOnlyOnce(content, FROM)) {
            int index = content.indexOf(FROM);
            String strTemp = content.substring(index);
            Set<AHrefElement> childLinks = AHrefParser.ahrefParser(strTemp, null, null, getCharset(), Boolean.FALSE);
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

    private static void readContent2DB(String path) throws Exception {
        File filePath = new File(path);
        File[] files = filePath.listFiles();
        if (files != null && files.length > 0) {
            List<File> fileList = getFileList(files);
            List<File> dirList = getDirList(files);
            for (File file : fileList) {
                dealFile(file);
            }
            fileList.clear();
            fileList = null;

            for (File file : dirList) {
                readContent2DB(file.getPath());
            }
            dirList.clear();
            dirList = null;
        }
        files = null;
    }

    private static void dealFile(File file) throws IOException, ParserException {
        boolean ok = Boolean.TRUE;
        int id = Integer.parseInt(FileUtil.getFileNameWithoutExt(file.getName()));
        String content = FileUtil.getFileContent(file, getCharset());
        try {
            content = StringUtil.subString(content, startList, endList);
        } catch (Exception e) {
            ok = Boolean.FALSE;
            FileUtil.writeFileAppend(basePath + "/checkResult.log", file.getAbsolutePath());
        }
        if (ok && !getIsCheckFile()) {
            if (content.indexOf("<script") > 0) {
                content = StringUtil.removeScript(content);
            }
            content = UrlUtil.replaceHref2GoUrl(content, getCharset());
            content = changeFromLinkText(content);
            content = content.replace("</script>", "");

            AddonarticleDO addonarticleDO = new AddonarticleDO();
            addonarticleDO.setAid(id);
            addonarticleDO.setBody(content);
            addonarticleDAO.updateBodyByAid(addonarticleDO);
        }

    }

    /**
     * 获取文章列表，不包括文件夹
     * 
     * @param files
     * @return
     */
    private static List<File> getFileList(File[] files) {
        List<File> fileList = new ArrayList<File>();
        for (File file : files) {
            if (file.isFile()) {
                Matcher matcher = articleNamePattern.matcher(file.getName());
                if (matcher.find()) {
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }

    /**
     * 获取文件夹的list列表，不包括文件夹
     * 
     * @param files
     * @return
     */
    private static List<File> getDirList(File[] files) {
        List<File> dirList = new ArrayList<File>();
        for (File file : files) {
            if (file.isDirectory() && !file.getName().endsWith(".svn")) {
                dirList.add(file);
            }
        }
        return dirList;
    }

    private static String getTablePrefix() {
        if (System.getProperty("tablePrefix") != null) {
            tablePrefix = System.getProperty("tablePrefix");
        }
        return tablePrefix;
    }

    private static String getCharset() {
        if (System.getProperty("charset") != null) {
            charset = System.getProperty("charset");
        }
        return charset;
    }

    private static String getBasePath() {
        if (System.getProperty("basePath") != null) {
            basePath = System.getProperty("basePath");
        }
        return basePath;
    }

    private static boolean getIsCheckFile() {
        if (System.getProperty("isCheckFile") != null) {
            isCheckFile = Boolean.parseBoolean(System.getProperty("isCheckFile"));
        }
        return isCheckFile;
    }

}
