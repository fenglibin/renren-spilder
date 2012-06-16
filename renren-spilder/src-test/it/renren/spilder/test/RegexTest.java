package it.renren.spilder.test;

import java.util.regex.Pattern;

public class RegexTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String urlMustInclude = "http://blog.yufeng.info/archives/*";
        Pattern must = null;
        if (urlMustInclude != null) {
            must = Pattern.compile(urlMustInclude);
        }
        String href = "http://blog.yufeng.info/archives/1363";
        boolean isMatch = must.matcher(href).find();/* 必须包括的字符串的正则，是否符合当前URL */
        System.out.println(isMatch);
    }

}
