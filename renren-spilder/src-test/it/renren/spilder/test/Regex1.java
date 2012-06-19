package it.renren.spilder.test;

import it.renren.spilder.util.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class Regex1 extends TestCase {

    public Regex1(String name){
        super(name);
    }

    public void test1() {
        String str = "http://www.cnblogs.com/mjc467621163/archive/2011/07/1222.html";
        String regEx = "([0-9]){4}[/]([0-9]){2}[/]([0-9]){2}[/]([0-9])*.html"; // 表示a或f
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        boolean result = m.find();
        assertFalse(result);
    }

    public void test2() {
        String str = "http://www.jz123.cn/text/1735169.html";
        String regEx = "text[/]([0-9]){4}([0-9])*.html"; // 表示a或f
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        boolean result = m.find();
        assertTrue(result);
    }

    public void test3() {
        String str = "http://developer.51cto.com/art/201108/285930.htm";
        str = "http://notice501.blog.51cto.com/3428502/640960";
        String regEx = "(art[/]([0-9]){6}[/]([0-9])*.htm)|(blog.51cto.com/([0-9])*/([0-9])*)"; // 表示a或f
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        boolean result = m.find();
        assertTrue(result);
    }

    public void test4() {
        String str = "aabb51CTOccdd51cto";
        str = str.replace("51CTO", "");
        System.out.println(str);
    }

    public void test5() {
        String str = "http://my.oschina.net/ohcoding/blog/31886";
        String regEx = "blog/([0-9])*$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        boolean result = m.find();
        assertTrue(result);
    }

    public void test6() {
        String str = "http://www.web20share.com/2011/08/ewaimai-interview.html";
        String regEx = "([0-9]){4}/([0-9]){2}/\\S+.html$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        boolean result = m.find();
        assertTrue(result);
    }

    public void test7() {
        String str = "http://www.guao.hk/posts/stick-google-plus-buttons-on-your-pages-or-your-search-traffic-dies.html";
        String regEx = "http://www.guao.hk/posts/(.+).html$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        boolean result = m.find();
        assertTrue(result);
    }

    public void test8() {
        String str = "http://511ctoblog.blog.51cto.com/170459/448160";
        String regEx = "blog.51cto.com/([0-9])*/([0-9])*$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        boolean result = m.find();
        assertTrue(result);
    }

    public void test9() {
        String str = "http://www.eryi.org/blog/post/computer-time-201161-360.html";
        String regEx = "/blog/post/(.+).html$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        boolean result = m.find();
        assertTrue(result);
    }

    public void test10() {
        String str = "http://tech.sina.com.cn/iii/2012-06-12/09487257092.shtml";
        String regEx = "/([a-z]){1,3}/([0-9]){4}\\-([0-9]){2}\\-([0-9]){2}/([0-9])*.shtml$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        boolean result = m.find();
        assertTrue(result);
    }

    public void removeHreflink() {
        String str = "aa<a href=xx.htm title=t>xxxxxx</a><b>fuck</b>";
        str = StringUtil.removeHreflink(str);
        assertEquals(str, "aabbccdd51cto");
    }

    public void testNot() {
        String str = "jump.html?www.it";
        String regEx = "(?!jump)";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        boolean result = m.find();
        assertFalse(result);

    }

    public void test11() {
        String str = "93292.html";
        String regEx = "^[0-9]*\\.html";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        boolean result = m.find();
        assertTrue(result);
    }
}
