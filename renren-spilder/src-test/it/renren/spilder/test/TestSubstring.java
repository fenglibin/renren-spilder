package it.renren.spilder.test;

import it.renren.spilder.util.FileUtil;
import it.renren.spilder.util.StringUtil;

import java.io.IOException;

public class TestSubstring {

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
