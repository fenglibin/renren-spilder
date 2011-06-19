package it.renren.spilder.util.other;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpException;

import it.renren.spilder.parser.AHrefElement;
import it.renren.spilder.parser.AHrefParser;
import it.renren.spilder.util.FileUtil;
import it.renren.spilder.util.HttpClientUtil;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.log.Log4j;

/**
 * 根据CSDN博客专家页面，生成配置文件
 * 
 * @author Administrator
 */
public class MakeBLOGJAVA {

    private static Log4j log4j = new Log4j(MakeBLOGJAVA.class.getName());

    /**
     * @param args
     * @throws IOException
     * @throws HttpException
     */
    public static void main(String[] args) throws HttpException, IOException {
        // TODO Auto-generated method stub
        String bloglist = "http://www.blogjava.net/AllBloggers.aspx";
        String filename = "config/www.blogjava.net/#.xml";
        String modXmlFile = "config/www.blogjava.net/model.xml";
        String content = HttpClientUtil.getGetResponseWithHttpClient(bloglist, "gbk");
        content = StringUtil.subString(content, "<table align=\"center\" width=\"90%\">", "</table></form>");
        try {
            String modelContent = FileUtil.getFileContent(modXmlFile);
            List<AHrefElement> childLinks = AHrefParser.ahrefParser(content, "", "rss.aspx", "gbk", false);
            String pages = "1";
            for (AHrefElement link : childLinks) {
                try {
                    pages = "1";
                    String childUrl = link.getHref();
                    String thisfilename = filename.replace("#", FileUtil.getFileName(childUrl));
                    if(new File(thisfilename).exists()){
                        continue;
                    }
                    content = HttpClientUtil.getGetResponseWithHttpClient(childUrl + "default.html?page=1", "utf-8");
                    if (content.indexOf("<div class=\"pager\">") > 0) {
                        content = StringUtil.subString(content, "<div class=\"pager\">", ":");
                        pages = StringUtil.subString(content, "共", "页");
                    }
                    childUrl = childUrl + "default.html?page=(*)";
                    String thisContent = modelContent.replace("#url#", childUrl);
                    thisContent = thisContent.replace("#pages#", pages);
                    FileUtil.writeFile(thisfilename, thisContent);
                    log4j.logDebug(childUrl + ":" + pages);
                } catch (Exception e) {
                    log4j.logError(e);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log4j.logError(e);
        }
    }

}
