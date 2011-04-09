package it.renren.spilder.util.google;

import it.renren.spilder.main.Constants;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.log.Log4j;

import com.google.api.translate.Language;
import com.google.api.translate.Translate;

public class TranslatorUtil {

    private static Log4j log4j = new Log4j(TranslatorUtil.class.getName());

    /**
     * 翻译HTML内容
     * 
     * @param content
     * @param fromLanguage
     * @param toLanguage
     * @return
     * @throws Exception
     */
    public static String translateHTML(String content, String fromLanguage, String toLanguage) throws Exception {
        String[] contentArray = splitLargeContent(content);
        content = translate(contentArray, fromLanguage, toLanguage, false);
        return content;
    }

    /**
     * 翻译纯文件内容
     * 
     * @param content
     * @param fromLanguage
     * @param toLanguage
     * @return
     * @throws Exception
     */
    public static String translateText(String content, String fromLanguage, String toLanguage) throws Exception {
        String[] contentArray = splitContentByLine(content);
        content = translate(contentArray, fromLanguage, toLanguage, true);
        return content;
    }

    private static String translate(String[] contentArray, String fromLanguage, String toLanguage, boolean lineSeperator)
                                                                                                                         throws Exception {
        Translate.setHttpReferrer("http://www.google.com");
        Language from = getLanguage(fromLanguage);
        Language to = getLanguage(toLanguage);
        String content = "";
        for (int i = 0; i < contentArray.length; i++) {
            String tmp = contentArray[i];
            if (!StringUtil.isNull(tmp)) {
                content += Translate.translate(contentArray[i], from, to);
                if (lineSeperator && i < contentArray.length - 1) {
                    content += "\n";
                }
            }
        }
        return content;
    }

    private static Language getLanguage(String lang) {
        Language l = null;
        if (lang.equalsIgnoreCase("cn")) l = Language.CHINESE_SIMPLIFIED;
        else if (lang.equalsIgnoreCase("big5")) l = Language.CHINESE_TRADITIONAL;
        else if (lang.equalsIgnoreCase("en")) {
            l = Language.ENGLISH;
        } else if (lang.equalsIgnoreCase("jp")) {
            l = Language.JAPANESE;
        } else if (lang.equalsIgnoreCase("fr")) {
            l = Language.FRENCH;
        } else if (lang.equalsIgnoreCase("ge")) {
            l = Language.GERMAN;
        }
        return l;
    }

    /**
     * GOOGLE翻译内容，一次最多不能够超过5000字符，如果超过就会报内容越长的错误；该方法将内容超过5000的内容，拆分到字符串数组中，循环翻译。<BR>
     * 使用条款文档地址：http://code.google.com/intl/zh-CN/apis/ajaxlanguage/terms.html
     * 
     * @param content
     * @return
     */
    private static String[] splitLargeContent(String content) {
        String[] result = null;
        if (content.length() <= Constants.GOOGLE_TRANSLATE_MAX_CHARATER_LENGTH) {
            result = new String[1];
            result[0] = content;
            return result;
        }
        result = new String[(int) (content.length() / Constants.GOOGLE_TRANSLATE_MAX_CHARATER_LENGTH) + 1];
        content = content.replace("</P>", "</p>");
        content = content.replace("<BR>", "<br>");
        content = content.replace("<Br>", "<br>");
        content = content.replace("<bR>", "<br>");
        int index = 0;
        String pstr = "</p>";
        if (content.indexOf(pstr) < 0 && content.indexOf("<br>") > 0) {
            pstr = "<br>";
        }
        if (content.indexOf(pstr) >= 0) {
            while (content.length() > Constants.GOOGLE_TRANSLATE_MAX_CHARATER_LENGTH) {
                String str1 = content.substring(0, Constants.GOOGLE_TRANSLATE_MAX_CHARATER_LENGTH);
                int location = getLastLineSeprator(str1, pstr);
                str1 = content.substring(0, location);
                result[index] = str1;
                content = content.substring(location);
                index++;
            }
            result[index] = content;
        } else {
            while (content.length() > Constants.GOOGLE_TRANSLATE_MAX_CHARATER_LENGTH) {
                String str1 = content.substring(0, Constants.GOOGLE_TRANSLATE_MAX_CHARATER_LENGTH);
                int location = Constants.GOOGLE_TRANSLATE_MAX_CHARATER_LENGTH;
                str1 = content.substring(0, location);
                result[index] = str1;
                content = content.substring(location);
                index++;
            }
            result[index] = content;
        }
        return result;
    }

    /**
     * 将内容按行分成字符串数组中
     * 
     * @param content
     * @return
     */
    private static String[] splitContentByLine(String content) {
        String[] result = null;
        result = content.split("\n");
        return result;
    }

    private static int getLastLineSeprator(String content, String lineSeprator) {
        int location = 0;
        if (content.indexOf(lineSeprator) > 0) {
            while (content.indexOf(lineSeprator, location + lineSeprator.length()) > 0) {
                location = content.indexOf(lineSeprator, location + lineSeprator.length());
            }
        } else {
            location = content.length() - 1;
        }
        return location;
    }

    public static void main(String[] args) {
        String content = "<br><p>以下说明针对此 API 当前支持的以下印度语言 - 孟加拉语 (bn)、古吉拉特语 (gu)、北印度语 (hi)、卡纳达语 (kn)、马拉雅拉姆语 (ml)、马拉地语 (mr)、尼泊尔语 (ne)、旁遮普语 (pa)、泰米尔语 (ta)、泰卢固语 (te) 和乌尔都语 (ur)。如果在 Windows Vista/XP/2000 中使用 Internet Explorer 6 及更高版本，则在查看和编辑采用以上大多数语言的文本时，应该不会遇到问题。如果 Mozilla Firefox 的版本低于 3.0，则需要支持复杂文本布局，否则可能无法正确显示印度语文本。默认情况下，通常关闭对复杂文本布局的支持，但是<a href=\"http://en.wikipedia.org/wiki/Wikipedia:Enabling_complex_text_support_for_Indic_scripts\">此 Wikipedia 文章</a>详细介绍了在各种操作系统上将其打开的方法。即使已启用复杂脚本显示，包含 chillu 的马拉雅拉姆语字符在某些系统上可能仍无法正确显示，在这种情况下，您可以参考<a href=\"http://varamozhi.wikia.com/wiki/Help:Contents/Unicode:_How_To\">此 Wikia 文章</a>来获得正确设置的解决方案。</p></dd>";
        String[] str = splitLargeContent(content);
        for (int i = 0; i < str.length; i++) {
            log4j.logDebug(str[i]);
        }
    }
}
