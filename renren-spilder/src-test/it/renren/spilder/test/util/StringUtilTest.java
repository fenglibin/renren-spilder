package it.renren.spilder.test.util;

import it.renren.spilder.util.StringUtil;
import junit.framework.TestCase;

import org.junit.Test;

public class StringUtilTest extends TestCase {

    @Test
    public void testRemoveScriptAndHrefTags() {
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
}
