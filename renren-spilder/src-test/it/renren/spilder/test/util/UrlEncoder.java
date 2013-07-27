package it.renren.spilder.test.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import junit.framework.TestCase;

import org.junit.Test;

public class UrlEncoder extends TestCase {

    @Test
    public void testEncode() throws UnsupportedEncodingException {
        String url = "http://blog.csdn.net/tag/details.html?tag=Windows Phone";
        url = URLEncoder.encode(url, "utf-8");
        System.out.print(url);
    }

    @Test
    public void testChinese() throws UnsupportedEncodingException {

        String chineseStr = "https://www.google.com.hk/#newwindow=1&safe=strict&q=java+%E4%B8%AD%E6%96%87%E5%88%A4%E6%96%AD&oq=java+%E4%B8%AD%E6%96%87%E5%88%A4%E6%96%AD&gs_l=serp.3..0j0i8i30.50922.50922.18.51319.1.1.0.0.0.0.132.132.0j1.1.0....0...1c..19.serp.LEG6j9BOkgk&bav=on.2,or.&bvm=bv.48705608,d.dGI&fp=71b6aa47ba050503&biw=1280&bih=602";
        StringBuffer utf8Str = new StringBuffer();
        char[] charArray = chineseStr.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if ((charArray[i] >= 0x4e00) && (charArray[i] <= 0x9fbb)) {
                utf8Str.append(URLEncoder.encode(String.valueOf(charArray[i]), "utf-8"));
            } else {
                utf8Str.append(charArray[i]);
            }
        }
        System.out.println(utf8Str.toString());
        System.out.println(URLDecoder.decode(utf8Str.toString(), "utf-8"));
    }
}
