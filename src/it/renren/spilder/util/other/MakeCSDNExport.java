package it.renren.spilder.util.other;

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
public class MakeCSDNExport {

    private static Log4j log4j = new Log4j(MakeCSDNExport.class.getName());

    /**
     * @param args
     * @throws IOException 
     * @throws HttpException 
     */
    public static void main(String[] args) throws HttpException, IOException {
        // TODO Auto-generated method stub
        String csdn_export_html = "http://blog.csdn.net/MoreExpert.html";
        // String filename = "E:/work/mywork/renren-spilder/config/doing/rule_csdn_blog_#_default.xml";
        // String modXmlFile = "E:/work/mywork/renren-spilder/config/doing/rule_csdn_blog_(model)_default.xml";
        String filename = "Z:/proc/test/renren-spilder/config/doing/rule_csdn_blog_#_default.xml";
        String modXmlFile = "Z:/proc/test/renren-spilder/config/doing/rule_csdn_blog_(model)_default.xml";
        String content = HttpClientUtil.getGetResponseWithHttpClient(csdn_export_html, "utf-8");
        content = StringUtil.subString(content, "<div class=\"allBlogs\">", "<div class=\"clear\"></div>");
        try {
            String modelContent = FileUtil.getFileContent(modXmlFile);
            List<AHrefElement> childLinks = AHrefParser.ahrefParser(content, "blog", "#", "utf-8", false);
            String pages = "1";
            for (AHrefElement link : childLinks) {
                try {
                    pages = "1";
                    String childUrl = link.getHref();
                    String thisfilename = filename.replace("#", FileUtil.getFileName(childUrl));
                    content = HttpClientUtil.getGetResponseWithHttpClient(childUrl, "utf-8");
                    if (content.indexOf("<div class=\"pagelist\">") > 0) {
                        content = StringUtil.subString(content, "<div class=\"pagelist\">", "</div>");
                        pages = StringUtil.subString(content, "，共", "页");
                    } else {
                        pages = "1";
                    }
                    String thisContent = modelContent.replace("#url#", childUrl + "?PageNumber=(*)");
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
