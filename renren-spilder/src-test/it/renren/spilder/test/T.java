package it.renren.spilder.test;

import it.renren.spilder.main.Constants;
import it.renren.spilder.util.FileUtil;
import it.renren.spilder.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class T {

    /**
     * @param args
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
        // TODO Auto-generated method stub
        test5();
    }

    public static void test1() {
        String childUrl = "";
        String suffix = ".html";
        int currentSeparatePage = 2;
        String url = "http://www.163.com/aa/bb.html?abc=true";
        String pageUrlName = StringUtil.subStringFromLastStart(url, "/", suffix);
        System.out.println(pageUrlName);
        String urlHead = StringUtil.substringBeforeLastWithSeparator(url, "/");
        System.out.println(urlHead);
        String param = StringUtil.subStringFromLastStart(url, suffix, null);
        System.out.println(param);
        childUrl = urlHead + pageUrlName + "_" + currentSeparatePage + suffix + param;
        System.out.println(childUrl);
    }

    public static void test2() {
        String str = "aa1111bb222aa3333bb4444bb";
        str = StringUtil.subStringFromLastStart(str, "aa", "bb");
        System.out.println(str);
    }

    public static void test3() {
        String imageName = "tablet2.108-550x411.png";
        imageName = "tablet2png";
        String litPicName = "";
        String ext = FileUtil.getFileExtensation(imageName);
        String filePrefix = imageName.replace(Constants.DOT + ext, "");
        litPicName = filePrefix + "-lp" + Constants.DOT + ext;
        System.out.println(litPicName);
    }

    public static void test4() throws UnsupportedEncodingException {
        URLDecoder decoder = new URLDecoder();
        String result = decoder.decode("%E5%90%84%E5%A4%A7%E6%90%9C%E7%B4%A2%E5%BC%95%E6%93%8E%E6%9B%B4%E6%8D%A2%E5%9B%BD%E5%BA%86logo.jpg",
                                       "utf-8");
        System.out.println(result);
    }

    public static void test5() throws UnsupportedEncodingException {
        String str = URLEncoder.encode("www.renren.it/a/JAVAbiancheng/j2ee/2012/0617/131470.html?aaa=bbb", "utf-8");
        System.out.println(str);
    }

}
