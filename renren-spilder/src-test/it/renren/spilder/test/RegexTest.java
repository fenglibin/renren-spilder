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
        boolean isMatch = must.matcher(href).find();/* ����������ַ����������Ƿ���ϵ�ǰURL */
        System.out.println(isMatch);
    }

}
