package net.fanli7.util;

import it.renren.timer.util.FileUtil;
import it.renren.timer.util.StringUtil;

import java.io.IOException;

/**
 * 获取一起发团购活动的LOGO
 * 
 * @author Administrator 2012-11-19 下午09:42:58
 */
public class GetTuanLog {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        String str = FileUtil.read("c:/yiqifatuan.txt");
        while (str.indexOf("<img src=\"") > 0) {
            String img = StringUtil.subString(str, "<img src=\"", "\"");
            String title = StringUtil.subString(str, "<p>", "</p>");
            title = title.replace("CPS", "");
            String sql = "update fanli_mall set img='" + img + "' where title='" + title + "';";
            System.out.println(sql);
            str = StringUtil.subString(str, "</p>", null);
        }
    }

}
