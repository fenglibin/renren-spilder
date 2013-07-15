package it.renren.spilder.util.other;

import it.renren.spilder.parser.AHrefElement;
import it.renren.spilder.parser.AHrefParser;
import it.renren.spilder.util.FileUtil;
import it.renren.spilder.util.HttpClientUtil;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.log.Log4j;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.commons.httpclient.HttpException;

/**
 * 根据CSDN博客专家页面，生成配置文件
 * 
 * @author Administrator
 */
public class MakeCNBlogs {

    private static Log4j log4j = new Log4j(MakeCNBlogs.class.getName());

    /**
     * @param args
     * @throws IOException
     * @throws HttpException
     */
    public static void main(String[] args) throws HttpException, IOException {
        // TODO Auto-generated method stub
        String bloglist = "http://www.cnblogs.com/AllBloggers.aspx";
        String filename = "config/cnblogs/#.xml";
        String modXmlFile = "config/cnblogs/model.xml";
        String content = HttpClientUtil.getGetResponseWithHttpClient(bloglist, "utf-8");
        content = StringUtil.subString(content, "<table align=\"center\" width=\"90%\">", "</table>");
        try {
            String modelContent = FileUtil.getFileContent(modXmlFile);
            Set<AHrefElement> childLinks = AHrefParser.ahrefParser(content, "", "rss.aspx", "utf-8", false);
            String pages = "1";
            for (AHrefElement link : childLinks) {
                try {
                    pages = "10";
                    String childUrl = link.getHref();
                    String thisfilename = filename.replace("#", FileUtil.getFileName(childUrl));
                    if (new File(thisfilename).exists()) {
                        continue;
                    } else {
                        // 默认最多10页，不去获取具体的用户有多少页了
                        // // 去掉下面的注释就可以获取详细的页数
                        // content = HttpClientUtil.getGetResponseWithHttpClient(childUrl + "default.html?page=1",
                        // "utf-8");
                        // if (content.indexOf("<div class=\"pager\">") > 0) {
                        // content = StringUtil.subString(content, "<div class=\"pager\">", ":");
                        // pages = StringUtil.subString(content, "共", "页");
                        // }
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
