package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Regex1 {

    public static void main(String args[]) {
        test2();
    }

    private static void test1() {
        String str = "http://www.cnblogs.com/mjc467621163/archive/2011/07/1222.html";
        String regEx = "([0-9]){4}[/]([0-9]){2}[/]([0-9]){2}[/]([0-9])*.html"; // 表示a或f
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        boolean result = m.find();
        System.out.println(result);
    }

    private static void test2() {
        String str = "http://www.jz123.cn/text/1735169.html";
        String regEx = "text[/]([0-9]){4}([0-9])*.html"; // 表示a或f
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        boolean result = m.find();
        System.out.println(result);
    }
}
