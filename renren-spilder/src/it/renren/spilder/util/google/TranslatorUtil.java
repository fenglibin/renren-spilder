package it.renren.spilder.util.google;

import it.renren.spilder.main.Constants;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.log.Log4j;

import com.google.api.translate.Language;
import com.google.api.translate.Translate;

public class TranslatorUtil {

    private static Log4j log4j = new Log4j(TranslatorUtil.class.getName());

    /**
     * ����HTML����
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
     * ���봿�ļ�����
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
     * GOOGLE�������ݣ�һ����಻�ܹ�����5000�ַ�����������ͻᱨ����Խ���Ĵ��󣻸÷��������ݳ���5000�����ݣ���ֵ��ַ��������У�ѭ�����롣<BR>
     * ʹ�������ĵ���ַ��http://code.google.com/intl/zh-CN/apis/ajaxlanguage/terms.html
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
     * �����ݰ��зֳ��ַ���������
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
        String content = "<br><p>����˵����Դ� API ��ǰ֧�ֵ�����ӡ������ - �ϼ����� (bn)���ż������� (gu)����ӡ���� (hi)�����ɴ��� (kn)����������ķ�� (ml)���������� (mr)���Ჴ���� (ne)���������� (pa)��̩�׶��� (ta)��̩¬���� (te) ���ڶ����� (ur)������� Windows Vista/XP/2000 ��ʹ�� Internet Explorer 6 �����߰汾�����ڲ鿴�ͱ༭�������ϴ�������Ե��ı�ʱ��Ӧ�ò����������⡣��� Mozilla Firefox �İ汾���� 3.0������Ҫ֧�ָ����ı����֣���������޷���ȷ��ʾӡ�����ı���Ĭ������£�ͨ���رնԸ����ı����ֵ�֧�֣�����<a href=\"http://en.wikipedia.org/wiki/Wikipedia:Enabling_complex_text_support_for_Indic_scripts\">�� Wikipedia ����</a>��ϸ�������ڸ��ֲ���ϵͳ�Ͻ���򿪵ķ�������ʹ�����ø��ӽű���ʾ������ chillu ����������ķ���ַ���ĳЩϵͳ�Ͽ������޷���ȷ��ʾ������������£������Բο�<a href=\"http://varamozhi.wikia.com/wiki/Help:Contents/Unicode:_How_To\">�� Wikia ����</a>�������ȷ���õĽ��������</p></dd>";
        String[] str = splitLargeContent(content);
        for (int i = 0; i < str.length; i++) {
            log4j.logDebug(str[i]);
        }
    }
}
