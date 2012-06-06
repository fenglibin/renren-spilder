package it.renren.spilder.util.wash;

import it.renren.spilder.util.StringUtil;

public class WashUtil {

    /**
     * 水洗数据。 将采集业的数据，在数据头部被当前网站的信息，以及在每个分段符中增加当前网站的信息作为分隔符。 这个主要的目的就是骗过搜索搜索引擎
     * 
     * @param body 原文体
     * @return 经过处理后的文休
     */
    public static String washData(String body, String insertStr) {
        if (body.indexOf(insertStr) >= 0) {
            return body;
        }
        body = insertStr + body + insertStr;
        body = body.replace("</P>", "</p>");
        body = body.replace("<BR>", "<br>");
        body = body.replace("<Br>", "<br>");
        body = body.replace("<bR>", "<br>");
        StringBuffer realBody = new StringBuffer(body);
        int index = 0;
        String pstr = "</p>";
        if (body.indexOf(pstr) < 0 && body.indexOf("<br>") > 0) {
            pstr = "<br>";
        }
        while ((index = body.indexOf(pstr)) > 0) {
            String bstr = StringUtil.blankString(insertStr.length() + pstr.length());
            body = bstr + body.substring(0, index - 1) + body.substring(index + pstr.length() - 1);
            realBody = realBody.insert(index + pstr.length(), insertStr);
        }
        return realBody.toString();
    }
}
