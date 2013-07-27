package it.renren.spilder.test.util;

import it.renren.spilder.parser.AHrefElement;
import it.renren.spilder.parser.AHrefParser;
import it.renren.spilder.util.HttpClientUtil;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.UrlUtil;

import java.util.Set;

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
        assertEquals(str, "qqqq<div></div><p><br />uuuu");
    }

    @Test
    public void testSubstring() {
        String str = "http://www.abc.com/1.html";
        str = str.substring(7);
        assertEquals(str, "www.abc.com/1.html");
    }

    @Test
    public void testRemoveProtocol() {
        String str = "http://www.abc.com/1.html";
        str = UrlUtil.removeProtocol(str);
        assertEquals(str, "www.abc.com/1.html");

        str = "https://www.abc.com/1.html";
        str = UrlUtil.removeProtocol(str);
        assertEquals(str, "www.abc.com/1.html");
    }

    @Test
    public void testChangeFromLinkText() {
        String url = "http://www.renren.it/a/bianchengyuyan/flex/2011/1121/109809.html";
        String encode = "gbk";

        url = "http://renren.it/a/bianchengyuyan/xml/2012/0606/168099.html";
        encode = "utf8";

        String from = "From£º";
        try {
            String str = HttpClientUtil.getGetResponseWithHttpClient(url, encode);
            if (StringUtil.checkExistOnlyOnce(str, from)) {
                int index = str.indexOf(from);
                String strTemp = str.substring(index);
                Set<AHrefElement> childLinks = AHrefParser.ahrefParser(strTemp, null, null, UrlUtil.DEFAULT_CHARSET,
                                                                       Boolean.FALSE);
                String strTemp2 = strTemp;
                for (AHrefElement href : childLinks) {
                    strTemp2 = strTemp2.replace(href.getHrefText() + "<", "<font color=#CCCCCC>Network</font><");
                    break;
                }
                str = str.replace(strTemp, strTemp2);
            }
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testCheckExistOnlyOnce() {
        String find = "From£º";
        String str = "hello baby,I love you baby,hehe," + find + ",you are right";
        Boolean b = StringUtil.checkExistOnlyOnce(str, find);
        assertTrue(b);

        str = "hello baby,I love " + find + " you baby,hehe," + find + ",you are right";
        b = StringUtil.checkExistOnlyOnce(str, find);
        assertFalse(b);
    }
}
