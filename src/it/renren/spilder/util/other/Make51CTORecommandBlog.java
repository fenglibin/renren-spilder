package it.renren.spilder.util.other;

import java.util.List;

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
public class Make51CTORecommandBlog {

    private static Log4j log4j = new Log4j(Make51CTORecommandBlog.class.getName());

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String blogs_html = "http://blog.51cto.com/blogcommend/3/";
        String filename = "E:/work/mywork/renren-spilder/config/blog.51cto.com/rule_#.xml";
        String modXmlFile = "E:/work/mywork/renren-spilder/config/blog.51cto.com/rule_51cto_blog_(model).xml";
        String charset = "utf-8";
        // String filename = "Z:/proc/test/renren-spilder/config/doing/rule_csdn_blog_#_default.xml";
        // String modXmlFile = "Z:/proc/test/renren-spilder/config/doing/rule_csdn_blog_(model)_default.xml";
        String content = "";
        for (int i = 1; i <= 21; i++) {// 51CTO推荐博客，目前共21页
            content = HttpClientUtil.getGetResponseWithHttpClient(blogs_html + i, charset);
            content = StringUtil.subString(content,
                                           "<td colspan=\"5\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">",
                                           "</table>");
            try {
                String modelContent = FileUtil.getFileContent(modXmlFile);
                List<AHrefElement> childLinks = AHrefParser.ahrefParser(content, "", "", charset, false);
                String pages = "1";
                String uid = "";
                for (AHrefElement link : childLinks) {
                    try {
                        pages = "1";
                        String childUrl = link.getHref();
                        if (!childUrl.endsWith(".com")) {
                            continue;
                        }
                        String thisfilename = filename.replace("#", childUrl.replace("http://", ""));
                        content = HttpClientUtil.getGetResponseWithHttpClient(childUrl, charset);
                        uid = StringUtil.subString(content, "../rss.php?uid=", "\"");
                        if (content.indexOf("( 1/") > 0) {
                            pages = StringUtil.subString(content, "( 1/", " )");
                        } else {
                            pages = "1";
                        }
                        String thisContent = modelContent.replace("#url#", childUrl + "/" + uid + "/p-(*)");
                        thisContent = thisContent.replace("#pages#", pages);
                        FileUtil.writeFile(thisfilename, thisContent);
                        log4j.logDebug(childUrl + ":" + pages + ":" + uid);
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

}
