package weiboautoman.timer.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

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
     * 字符串截取，与subString不同的是，开始字符串lastStart不是从字符串开始查找到的第一个，而是找到的最后一个lastStart字符串，然后再截取到从这个时候开始的end字符串为止，如下示例：<br>
     * 如有一个字符串是：aa1111bb222aa3333bb4444bb，传入的参数lastStart为aa，end为bb，则时得到的结果为:<br>
     * <b>3333</b>
     * 
     * @param src 源字符串
     * @param start 当前字符串中的最后一个开始字符串
     * @param end 结尾字符串
     * @return
     */
    public static String subStringFromLastStart(String src, String lastStart, String end) {
        src = src.substring(src.lastIndexOf(lastStart)).substring(lastStart.length());
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
        if (!isNull(start)) {
            src = src.indexOf(start) > -1 ? src.substring(src.indexOf(start)).substring(start.length()) : src;
        }
        if (!isNull(end)) {
            src = src.indexOf(end) > -1 ? src.substring(0, src.indexOf(end)) : src;
        }
        return src;
    }

    /**
     * 根据传入的内容，去掉所有html标签，目前此方法的实现来源于这里：http://www.rgagnon.com/javadetails/java-0424.html
     * 
     * @param htmlSource
     * @return
     */
    public static String removeHtmlTags(String htmlSource) {
        return htmlSource.replaceAll("\\<.*?>", "");
    }

    /**
     * 去掉script
     * 
     * @param htmlSource
     * @return
     */
    public static String removeScript(String htmlSource) {
        return htmlSource.replaceAll("\\<script.*?/script>", "");
    }

    /**
     * 去除超链接
     * 
     * @param htmlSource
     * @return
     */
    public static String removeHreflink(String htmlSource) {
        htmlSource = htmlSource.replaceAll("\\<a[^>]*>", "");
        htmlSource = htmlSource.replaceAll("\\</a>", "");
        return htmlSource;
    }

    /**
     * 删除script标签及超链接
     * 
     * @param htmlSource
     * @return
     */
    public static String removeScriptAndHrefTags(String htmlSource) {
        htmlSource = removeScript(htmlSource);
        htmlSource = removeHreflink(htmlSource);
        return htmlSource;
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

    /**
     * 根据指定的分隔符，从源字符串查找到最后一个分隔符，然后返回此分隔符前面的内容，如果找不到此分隔符则返回原字符串，
     * 
     * @param str
     * @param separator
     * @return
     */
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
     * 根据指定的分隔符，从源字符串查找到最后一个分隔符，然后返回此分隔符前面的内容，如果找不到此分隔符则返回原字符串。<br>
     * 此时会把分隔符加到分隔后得到的字符串再返回，前提是找到了此分隔符，否则返回原字符串，不加上分隔符
     * 
     * @param str
     * @param separator
     * @return
     */
    public static String substringBeforeLastWithSeparator(String str, String separator) {
        String result = substringBeforeLast(str, separator);
        if (!result.equals(str)) {
            result += separator;
        }
        return result;
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
        if (!StringUtil.isNull(from) && !StringUtil.isNull(to)) {
            if (isIssRegularExpression) {
                content = content.replaceAll(from, to);
            } else {
                content = content.replace(from, to);
            }
        }
        return content;
    }
}
