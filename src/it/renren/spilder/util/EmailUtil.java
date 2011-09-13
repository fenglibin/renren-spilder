package it.renren.spilder.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtil {

    private static String saveFile = "email.txt";

    private static void getEmailAdress(String content) throws IOException {
        Map<String, String> emailMap = new HashMap<String, String>();
        String email = null;
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = p.matcher(content);
        while (m.find()) {
            email = m.group();
            emailMap.put(email, email);
        }
        Iterator<String> it = emailMap.keySet().iterator();
        while (it.hasNext()) {
            email = it.next();
            System.out.println(email);
            FileUtil.writeFileAppend(saveFile, email);
        }
    }

    public static void main(String[] args) throws IOException {

        String email = "中国人是678xx.abc@163.com或者是xxx@gmail.com.cn.cn";
        String url = "";
        for (int i = 1; i <= 2; i++) {
            url = "http://club.tnc.com.cn/archiver/tid-468558-page-" + i+".html";
            System.out.println(url);
            email = UrlUtil.getContentByURL(url, "gbk");
            getEmailAdress(email);
        }
//         url="http://www.lance8.cn/com/Lance/article.asp?id=53";
//         email = UrlUtil.getContentByURL(url, "gbk");
//         //email = email.replace("&#64;", "@");
//         // try {
//         // email=FileUtil.getFileContent("/home/fenglibin/tmp/t.html");
//         // } catch (Exception e) {
//         // // TODO Auto-generated catch block
//         // e.printStackTrace();
//         // }
//         getEmailAdress(email);
    }
}
