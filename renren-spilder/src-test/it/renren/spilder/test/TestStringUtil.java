package it.renren.spilder.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;
import it.renren.spilder.util.StringUtil;

public class TestStringUtil extends TestCase {

    @Test
    public void testAddStringBeforeAll() {
        // TODO Auto-generated method stub
        String source = "0aaaaaaa11cccccccccddddddddddeeeeeeeeee11ewerwersdfsdfsd11dffadfaaaaaaaa11cccccccccddddddddddeeeeeeeeee11ewerwersdfsdfsd11dffadfa";
        // String locateString="11";
        String locateString = "aa";
        String addString = "ZZ";
        source = StringUtil.addStringBeforeAll(source, locateString, addString);
        System.out.println(source);
    }

    @Test
    public void testSubString() {
        String str = "adfadfsdfasBBttttCCxaafdsfdsBBddadfasfsfdCCxxxx";
        System.out.println(StringUtil.subString(str, "BB", "CC"));
    }

    @Test
    public void testLoopSubString() {
        String replys = "adfadfsdfasBBttttCCxaafdsfdsBBddadfasfsfdCCxxxx";
        String start = "BB";
        String end = "CC";
        List<String> replysList = new ArrayList<String>();
        int index = -1;
        while (replys.indexOf(start) > 0) {
            String reply = StringUtil.subString(replys, start, end);
            replysList.add(reply);
            replys = replys.substring(replys.indexOf(end) + end.length());
        }
    }

    @Test
    public void testRemoveScript() {
        String str = "dasdf<script>alert('0')</script>AAAAAdasdf<script>alert('0')</script>xxxxxxxx";
        System.out.println(StringUtil.removeHtmlTags(str));
        System.out.println(StringUtil.removeScript(str));
    }
}
