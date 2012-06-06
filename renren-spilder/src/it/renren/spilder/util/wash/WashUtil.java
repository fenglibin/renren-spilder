package it.renren.spilder.util.wash;

import it.renren.spilder.util.StringUtil;

public class WashUtil {

    /**
     * ˮϴ���ݡ� ���ɼ�ҵ�����ݣ�������ͷ������ǰ��վ����Ϣ���Լ���ÿ���ֶη������ӵ�ǰ��վ����Ϣ��Ϊ�ָ����� �����Ҫ��Ŀ�ľ���ƭ��������������
     * 
     * @param body ԭ����
     * @return ��������������
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
