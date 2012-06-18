package it.renren.spilder.util;

import it.renren.spilder.main.Environment;
import it.renren.spilder.util.log.Log4j;

import java.util.ArrayList;
import java.util.List;

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

    public static boolean isEmpty(String str) {
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
     * �ַ�����ȡ����subString��ͬ���ǣ���ʼ�ַ���lastStart���Ǵ��ַ�����ʼ���ҵ��ĵ�һ���������ҵ������һ��lastStart�ַ�����Ȼ���ٽ�ȡ�������ʱ��ʼ��end�ַ���Ϊֹ������ʾ����<br>
     * ����һ���ַ����ǣ�aa1111bb222aa3333bb4444bb������Ĳ���lastStartΪaa��endΪbb����ʱ�õ��Ľ��Ϊ:<br>
     * <b>3333</b>
     * 
     * @param src Դ�ַ���
     * @param start ��ǰ�ַ����е����һ����ʼ�ַ���
     * @param end ��β�ַ���
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
        if (!isEmpty(start)) {
            src = src.indexOf(start) > -1 ? src.substring(src.indexOf(start)).substring(start.length()) : src;
        }
        if (!isEmpty(end)) {
            src = src.indexOf(end) > -1 ? src.substring(0, src.indexOf(end)) : src;
        }
        return src;
    }

    /**
     * ���ݴ�������ݣ�ȥ������html��ǩ��Ŀǰ�˷�����ʵ����Դ�����http://www.rgagnon.com/javadetails/java-0424.html
     * 
     * @param htmlSource
     * @return
     */
    public static String removeHtmlTags(String htmlSource) {
        return htmlSource.replaceAll("\\<.*?>", "");
    }

    // /**
    // * ȥ��script
    // *
    // * @param htmlSource
    // * @return
    // */
    // public static String removeScript(String htmlSource) {
    // htmlSource = htmlSource.replaceAll("<script.*</script>", "");
    // return htmlSource;
    // }

    /**
     * ȥ��script�����е�����
     * 
     * @param htmlSource
     * @return
     */
    public static String removeScript(String htmlSource) {
        htmlSource = htmlSource.replaceAll("<script.*</script>", "");
        int start = -1;
        int end = -1;
        String temp = null;
        String endScript = "</script>";
        while ((start = htmlSource.indexOf("<script")) >= 0) {
            end = htmlSource.indexOf(endScript);
            if (end > -1 && end > start) {
                temp = htmlSource.substring(start, end + endScript.length());
                htmlSource = htmlSource.replace(temp, "");
            }
        }
        return htmlSource;
    }

    /**
     * ȥ��������
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
     * ɾ��script��ǩ��������
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

    /**
     * ����ָ���ķָ�������Դ�ַ������ҵ����һ���ָ�����Ȼ�󷵻ش˷ָ���ǰ������ݣ�����Ҳ����˷ָ����򷵻�ԭ�ַ�����
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
     * ����ָ���ķָ�������Դ�ַ������ҵ����һ���ָ�����Ȼ�󷵻ش˷ָ���ǰ������ݣ�����Ҳ����˷ָ����򷵻�ԭ�ַ�����<br>
     * ��ʱ��ѷָ����ӵ��ָ���õ����ַ����ٷ��أ�ǰ�����ҵ��˴˷ָ��������򷵻�ԭ�ַ����������Ϸָ���
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
     * ��������ֵΪnull���򷵻�""�����򷵻�ԭֵ
     * 
     * @param str
     * @return
     */
    public static String returnBlankIfNull(String str) {
        return str == null ? "" : str;
    }

    /**
     * ��������ֵΪnull���򷵻�false�����򷵻���Ӧ��booleanֵ
     * 
     * @param str
     * @return
     */
    public static Boolean returnFalseIfNull(String str) {
        return str == null ? Boolean.FALSE : Boolean.parseBoolean(str);
    }

    /**
     * �Ӹ���������content�л�ȡ������start�ַ�����end�ַ������������ݣ�����start��end����Կ����Ƕദ��������List������ء�<br>
     * �����������contentΪ��adfadfsdfasBBttttCCxaafdsfdsBBddadfasfsfdCCxxxx��<br>
     * startΪ:BB<br>
     * endΪ��CC<br>
     * �򷵻ص�list�������������¼��tttt �� ddadfasfsfd.<br>
     * �ڻ�ȡʱ��end�ַ���������λ��һ��Ҫ����start������λ�ã������Դ���Ĭ������Բ��ɹ������������ѭ����
     * 
     * @param content ����ȡ�ظ�������
     * @param start һ���ظ��Ŀ�ʼ�ָ��ַ���
     * @param end һ���ظ��Ľ����ָ��ַ���
     * @param isFirstMainContent ��һ���Ƿ������ݣ�����̳�����ݺͻظ��ķָ�������һ���ģ���ʱ�����ַΪtrue������£���һ���Ϊ��ظ�������Ĳ���Ϊ�ظ�
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
     * ���������ݽ������⴦�������������滻������Ҳͨ��ʵ�ֵ�HANDLER����Handler��Ҫʵ�ֽӿ�{@link it.renren.spilder.task.handler.Handler Handler}
     * 
     * @param content
     * @param from
     * @param to
     * @param isIssRegularExpression
     * @return
     */
    public static String replaceContent(String content, String from, String to, boolean isIssRegularExpression) {
        if (!StringUtil.isEmpty(from) && !StringUtil.isEmpty(to)) {
            if (isIssRegularExpression) {
                content = content.replaceAll(from, to);
            } else {
                content = content.replace(from, to);
            }
        }
        return content;
    }

    /**
     * ��ȡ��ǰ׺<br>
     * ����������д����ǰ׺�����������ȸ��������д���ı�ǰ׺Ϊ׼;���������û�д��룬�ٸ��������д���ı�ǰ׺Ϊ׼
     * 
     * @param tablePrefix
     * @return
     */
    public static String getTablePrefix(String tablePrefix) {
        if (!StringUtil.isEmpty(Environment.tablePrefix)) {
            tablePrefix = Environment.tablePrefix;
        } else if (StringUtil.isEmpty(tablePrefix)) {
            tablePrefix = "";
        }
        return tablePrefix;
    }

    /**
     * ��鱻�����ַ���(find)��ԭ�ַ���(source)���ǲ���ֻ������һ��
     * 
     * @param source ԭ�ַ���
     * @param find �������ַ���
     * @return
     */
    public static boolean checkExistOnlyOnce(String source, String find) {
        if (StringUtil.isEmpty(source) || StringUtil.isEmpty(find)) {
            return false;
        }
        int indexOf = source.indexOf(find);
        if (indexOf < 0) {
            return false;
        }
        int lastIndexOf = source.lastIndexOf(find);
        if (lastIndexOf < 0) {
            return false;
        }
        if (indexOf == lastIndexOf) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        String htmlSource = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'><html xmlns='http://www.w3.org/1999/xhtml'><head><meta http-equiv='Content-Type' content='text/html; charset=gb2312' /><title>���ӣ����ַ���ʱ��ת��Ϊtimestamp_����IT��</title><meta name='keywords' content='ת��,ʱ��,�ַ���,����,array,t' /><meta name='description' content='? $strtime = 2000-02-12 16:20:35; $array = explode(-,$strtime); $year = $array[0]; $month = $array[1]; $array = explode(:,$array[2]); $minute = $array[1]; $second = $array[2]; $array = explode( ,$array[0]); $day = $array[0]; $hour = $array[' /><link href='/templets/default/style/dedecms.css' rel='stylesheet' media='screen' type='text/css' /><link rel='stylesheet' href='/templets/default/style/dedecms_skins_0.css' type='text/css' id='cssfile' /><style>";
        htmlSource = removeHtmlTags(htmlSource);
        log4j.logDebug(htmlSource);
    }
}
