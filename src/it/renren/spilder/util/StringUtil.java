package it.renren.spilder.util;

import it.renren.spilder.util.log.Log4j;

public class StringUtil {

    private static Log4j log4j = new Log4j(StringUtil.class.getName());

    public static String blankString(int num) {
        String str = "";
        while (num > 0) {
            str += " ";
            num--;
        }
        return str;
    }

    public static boolean isNull(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

    /**
     * ��ԭʼ�ַ����У�ȡ�ô�start�ַ�����ʼ��������start�ַ�������end�ַ���Ϊֹ�������е��ַ�����str='aabbccddee',start='bb',end='ee',���ʱ���Ϊ'ccdd'��
     * ��ʼ�������ַ���������ڣ�����ͱ���
     * 
     * @param src Դ�ַ���
     * @param start ��ʼ�ַ���
     * @param end ��β�ַ���
     * @return ��ʼ�ַ�������β�ַ������м��ַ���
     * @throws Exception
     */
    public static String subString(String src, String start, String end) {
        src = src.substring(src.indexOf(start)).substring(start.length());
        if (end != null) {
            src = src.substring(0, src.indexOf(end));
        }
        return src;
    }

    /**
     * ��ԭʼ�ַ����У�ȡ�ô�start�ַ�����ʼ��������start�ַ�������end�ַ���Ϊֹ�������е��ַ�����str='aabbccddee',start='bb',end='ee',���ʱ���Ϊ'ccdd'��
     * �����ʼ�ַ��������ڣ���ʼ��Ϊԭ�ַ����ĵ�һ�ַ�����������ַ��������ڣ���Ϊ�ַ��������һ���ַ�����
     * 
     * @param src Դ�ַ���
     * @param start ��ʼ�ַ���
     * @param end ��β�ַ���
     * @return ��ʼ�ַ�������β�ַ������м��ַ���
     * @throws Exception
     */
    public static String subStringSmart(String src, String start, String end) {
        src = src.indexOf(start) > -1 ? src.substring(src.indexOf(start)).substring(start.length()) : src;
        if (end != null) {
            src = src.indexOf(end) > -1 ? src.substring(0, src.indexOf(end)) : src;
        }
        return src;
    }

    /**
     * ���ݴ�������ݣ�ȥ������html��ǩ
     * 
     * @param htmlSource
     * @return
     */
    public static String removeHtmlTags(String htmlSource) {
        int start = -1, end = -1;
        String temp;
        start = htmlSource.indexOf("<");
        if (start >= 0) {
            temp = htmlSource.substring(start);
            end = temp.indexOf(">") + start;
        }
        while (start >= 0 && end > 0) {
            temp = htmlSource.substring(start, end + 1);
            end = -1;
            htmlSource = htmlSource.replace(temp, "");
            start = htmlSource.indexOf("<");
            if (start >= 0) {
                temp = htmlSource.substring(start);
                end = temp.indexOf(">") + start;
            }
        }
        return htmlSource.trim();
    }

    public static void main(String[] args) {
        String htmlSource = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'><html xmlns='http://www.w3.org/1999/xhtml'><head><meta http-equiv='Content-Type' content='text/html; charset=gb2312' /><title>���ӣ����ַ���ʱ��ת��Ϊtimestamp_����IT��</title><meta name='keywords' content='ת��,ʱ��,�ַ���,����,array,t' /><meta name='description' content='? $strtime = 2000-02-12 16:20:35; $array = explode(-,$strtime); $year = $array[0]; $month = $array[1]; $array = explode(:,$array[2]); $minute = $array[1]; $second = $array[2]; $array = explode( ,$array[0]); $day = $array[0]; $hour = $array[' /><link href='/templets/default/style/dedecms.css' rel='stylesheet' media='screen' type='text/css' /><link rel='stylesheet' href='/templets/default/style/dedecms_skins_0.css' type='text/css' id='cssfile' /><style>";
        htmlSource = removeHtmlTags(htmlSource);
        log4j.logDebug(htmlSource);
    }

    /**
     * �ڡ�Դ�ַ������С������ҵ��ִ���֮ǰ���ڡ������ҵ��ַ�����֮ǰ���䡰���ӵ��ַ�������<br>
     * ע���÷�������BUG����֧�ֶ���������ַ�������һ��
     * 
     * @param source Դ�ַ���
     * @param locateString �����ҵ��ַ���
     * @param addString ���ӵ��ַ���
     * @return
     */
    public static String addStringBeforeAll(String source, String locateString, String addString) {
        int index = 0;
        StringBuffer realBody = new StringBuffer(source);
        String bstr = StringUtil.blankString(addString.length() + locateString.length());
        while ((index = source.indexOf(locateString)) >= 0) {
            source = bstr + source.substring(0, index - 1) + source.substring(index + locateString.length() - 1);
            realBody = realBody.insert(index, addString);
        }
        return realBody.toString();
    }

    public static String substringBeforeLast(String str, String separator) {
        if ((str == null) || (separator == null) || (str.length() == 0) || (separator.length() == 0)) {
            return str;
        }

        int pos = str.lastIndexOf(separator);

        if (pos == -1) {
            return str;
        }

        return str.substring(0, pos);
    }
}
