package it.renren.spilder.util;

import java.util.ArrayList;
import java.util.List;

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
     * 从原始字符串中，取得从start字符串开始但不包括start字符串，到end字符串为止其中所有的字符，如str='aabbccddee',start='bb',end='ee',则此时结果为'ccdd'。
     * 开始及结束字符串必须存在，否则就报错。
     * 
     * @param src 源字符串
     * @param start 开始字符串
     * @param end 结尾字符串
     * @return 开始字符串到结尾字符串的中间字符串
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
     * 从原始字符串中，取得从start字符串开始但不包括start字符串，到end字符串为止其中所有的字符，如str='aabbccddee',start='bb',end='ee',则此时结果为'ccdd'。
     * 如果开始字符串不存在，则开始处为原字符串的第一字符，如果结束字符串不存在，则为字符串的最后一个字符。果
     * 
     * @param src 源字符串
     * @param start 开始字符串
     * @param end 结尾字符串
     * @return 开始字符串到结尾字符串的中间字符串
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
     * 根据传入的内容，去掉所有html标签
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
        String htmlSource = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'><html xmlns='http://www.w3.org/1999/xhtml'><head><meta http-equiv='Content-Type' content='text/html; charset=gb2312' /><title>例子：把字符串时间转换为timestamp_人人IT网</title><meta name='keywords' content='转换,时间,字符串,例子,array,t' /><meta name='description' content='? $strtime = 2000-02-12 16:20:35; $array = explode(-,$strtime); $year = $array[0]; $month = $array[1]; $array = explode(:,$array[2]); $minute = $array[1]; $second = $array[2]; $array = explode( ,$array[0]); $day = $array[0]; $hour = $array[' /><link href='/templets/default/style/dedecms.css' rel='stylesheet' media='screen' type='text/css' /><link rel='stylesheet' href='/templets/default/style/dedecms_skins_0.css' type='text/css' id='cssfile' /><style>";
        htmlSource = removeHtmlTags(htmlSource);
        log4j.logDebug(htmlSource);
    }

    /**
     * 在“源字符串”中“被查找的字串”之前，在“被查找的字符串”之前补充“增加的字符串”。<br>
     * 注：该方法存在BUG，不支持多个被查找字符串连在一起。
     * 
     * @param source 源字符串
     * @param locateString 被查找的字符串
     * @param addString 增加的字符串
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

    /**
     * 如果传入的值为null，则返回""，否则返回原值
     * 
     * @param str
     * @return
     */
    public static String returnBlankIfNull(String str) {
        return str == null ? "" : str;
    }

    /**
     * 如果传入的值为null，则返回false，否则返回相应的boolean值
     * 
     * @param str
     * @return
     */
    public static Boolean returnFalseIfNull(String str) {
        return str == null ? Boolean.FALSE : Boolean.parseBoolean(str);
    }

    /**
     * 从给定的内容content中获取给定的start字符串到end字符串的所有内容，其中start和end的配对可以是多处，最终以List结果返回。<br>
     * 如给定的内容content为：adfadfsdfasBBttttCCxaafdsfdsBBddadfasfsfdCCxxxx，<br>
     * start为:BB<br>
     * end为：CC<br>
     * 则返回的list结果包括两条记录：tttt 和 ddadfasfsfd.<br>
     * 在获取时，end字符串所处的位置一定要大于start所处的位置，否则不以处理，默认来配对不成功，跳出处理的循环。
     * 
     * @param content 待获取回复的内容
     * @param start 一个回复的开始分隔字符串
     * @param end 一个回复的结束分隔字符串
     * @param isFirstMainContent 第一项是否主内容，如论坛的内容和回复的分隔符都是一样的，此时在这个址为true的情况下，第一项不做为后回复，后面的才做为回复
     * @return
     */
    public static List<String> getListFromStart2End(String content, String start, String end, boolean isFirstMainContent) {
        List<String> replysList = new ArrayList<String>();
        if (content.indexOf(start) < 0) {
            return replysList;
        }
        int index_start = -1;
        int num = 1;
        while ((index_start = content.indexOf(start)) > 0) {
            content = content.substring(index_start);
            int index_end = content.indexOf(end);
            if (index_end > 0) {
                String reply = StringUtil.subString(content, start, end);
                if (!isFirstMainContent || (isFirstMainContent && num > 1)) {
                    replysList.add(reply);
                }
                content = content.substring(content.indexOf(end) + end.length());
            } else {
                break;
            }
            num++;
        }
        return replysList;
    }

    /**
     * 对文章内容进行特殊处理，如文章内容替换，或者也通过实现的HANDLER处理，Handler需要实现接口{@link it.renren.spilder.task.handler.Handler Handler}
     * 
     * @param content
     * @param from
     * @param to
     * @param isIssRegularExpression
     * @return
     */
    public static String replaceContent(String content, String from, String to, boolean isIssRegularExpression) {
        if (!from.equals("") && !to.equals("")) {
            if (isIssRegularExpression) {
                content = content.replaceAll(from, to);
            } else {
                content = content.replace(from, to);
            }
        }
        return content;
    }
}
