package it.renren.spilder.test.util;

import it.renren.spilder.util.FileUtil;
import it.renren.spilder.util.StringUtil;

import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Test;

public class StringUtilTest extends TestCase {

    @Test
    public void testRemoveScript() {
        String str = "qqqq<div><script type=\"text/javascript\"><!--";
        str += "google_ad_client = \"ca-pub-1944176156128447\";";
        str += "google_ad_slot = \"5684583492\"";
        str += "google_ad_width = 468;";
        str += "google_ad_height = 60;";
        str += "//--></script>       <script type=\"text/javascript\" src=\"http://pagead2.googlesyndication.com/pagead/show_ads.js\">";
        str += "</script></div>";
        str += "<p><br />uuuu";
        str = StringUtil.removeScript(str);
        assertEquals(str, "qqqq<div>       </div><p><br />uuuu");
    }

    @Test
    public void trrrrrr1() {
        String str;
        try {
            str = FileUtil.read("d:/test/a.txt");
            str = StringUtil.removeScript(str);
            System.out.print(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(1, 1);
    }
}
