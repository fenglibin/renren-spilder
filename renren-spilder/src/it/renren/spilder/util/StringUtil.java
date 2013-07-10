package it.renren.spilder.util;

import it.renren.spilder.main.Constants;
import it.renren.spilder.main.Environment;
import it.renren.spilder.util.log.Log4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.htmlparser.util.ParserException;

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
     * �ַ�����ȡ
     * 
     * @param childContent
     * @param startList
     * @param endList
     * @return
     * @throws RuntimeException
     * @throws ParserException
     */
    public static String subString(String childContent, List<String> startList, List<String> endList) {
        int startSize = startList.size();
        for (int i = 0; i < startSize; i++) {
            try {
                childContent = StringUtil.subString(childContent, startList.get(i), endList.get(i));
                break;
            } catch (Exception e) {
                if (i + 1 == startSize) {
                    throw new RuntimeException(e);
                } else {
                    log4j.logDebug("�� " + (i + 1) + " �λ�ȡ�������ݳ���");
                }
            }
        }
        return childContent;
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
    public static String removeScriptByLoopFind(String htmlSource) {
        htmlSource = htmlSource.replaceAll("<script.*</script>", "");
        int start = -1;
        int end = -1;
        String temp = null;
        String endScript = "</script>";
        while ((start = htmlSource.indexOf("<script")) >= 0) {
            end = htmlSource.indexOf(endScript);
            if (end < 0) {
                break;
            }
            if (start < end) {
                temp = htmlSource.substring(start, end + endScript.length());
                htmlSource = htmlSource.replace(temp, "");
            } else if (start >= end) {
                break;
            }
        }
        return htmlSource;
    }

    /**
     * ȥ��script�����е�����
     * 
     * @param htmlSource
     * @return
     */
    public static String removeScript(String htmlSource) {
        htmlSource = htmlSource.replaceAll("<script([\\s\\S]*?)</script>", "");
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
     * �����滻
     * 
     * @param content
     * @param from
     * @param to
     * @return
     */
    public static String replaceContent(String content, String from, String to) {
        return replaceContent(content, from, to, Boolean.FALSE);
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
        List<String> fromList = new ArrayList<String>();
        List<String> toList = new ArrayList<String>();
        fromList.add(from);
        toList.add(to);
        return replaceContent(content, fromList, toList, isIssRegularExpression);
    }

    /**
     * �����滻�������б�����ݽ��н����滻���滻�ַ��б��뱻�滻�ַ����б������Ҫ��ͬ
     * 
     * @param content
     * @param fromList
     * @param toList
     * @return
     */
    public static String replaceContent(String content, List<String> fromList, List<String> toList) {
        return replaceContent(content, fromList, toList, Boolean.FALSE);
    }

    /**
     * �����滻�������б�����ݽ��н����滻���滻�ַ��б��뱻�滻�ַ����б������Ҫ��ͬ���ɸ���ָ���Ĳ��������Ƿ������滻
     * 
     * @param content
     * @param fromList
     * @param toList
     * @param isIssRegularExpression
     * @return
     */
    public static String replaceContent(String content, List<String> fromList, List<String> toList,
                                        boolean isIssRegularExpression) {
        if (fromList == null || fromList.size() == 0 || toList == null || toList.size() == 0) {
            return content;
        }
        if (fromList.size() != toList.size()) {
            throw new RuntimeException("The sourceList size must be the same as the descList.");
        }
        int index = 0;
        for (String from : fromList) {
            String to = toList.get(index);
            index++;
            if (from == null || "".equals(from) || to == null || "".equals(to)) {
                continue;
            }
            if (content.indexOf(from) >= 0) {
                if (!StringUtil.isEmpty(from) && !StringUtil.isEmpty(to)) {
                    if (isIssRegularExpression) {
                        content = content.replaceAll(from, to);
                    } else {
                        content = content.replace(from, to);
                    }
                }
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

    public static void writeGetImageToFile(String srcUrl, String fileSavePath, String fileName) throws IOException {
        String err = "#" + srcUrl + "===" + fileSavePath + "===" + DateUtil.getNow(null) + "\n";
        err += "cd " + fileSavePath + "\n";
        err += "rm -f " + fileName + "\n";
        err += "wget " + srcUrl + "\n";
        err += "cd /home/fenglibin/mysoft\n";
        err += "java -Xmn130m -Xms256m -Xmx400m -cp renren.it_spilder.jar it.renren.spilder.util.wash.GenLitImage savePath="
               + fileSavePath + "/ filename=" + fileName + "\n";
        err += "cd -\n";
        try {
            FileUtil.writeFileAppend(Constants.notGetImagesUrlSaveFile, err);
        } catch (Exception e) {
            log4j.logError("srcUrl:" + srcUrl + ",fileSavePath:" + fileSavePath, e);
        }
    }

    public static void main(String[] args) {
        String htmlSource = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'><html xmlns='http://www.w3.org/1999/xhtml'><head><meta http-equiv='Content-Type' content='text/html; charset=gb2312' /><title>���ӣ����ַ���ʱ��ת��Ϊtimestamp_����IT��</title><meta name='keywords' content='ת��,ʱ��,�ַ���,����,array,t' /><meta name='description' content='? $strtime = 2000-02-12 16:20:35; $array = explode(-,$strtime); $year = $array[0]; $month = $array[1]; $array = explode(:,$array[2]); $minute = $array[1]; $second = $array[2]; $array = explode( ,$array[0]); $day = $array[0]; $hour = $array[' /><link href='/templets/default/style/dedecms.css' rel='stylesheet' media='screen' type='text/css' /><link rel='stylesheet' href='/templets/default/style/dedecms_skins_0.css' type='text/css' id='cssfile' /><style>";
        htmlSource = removeHtmlTags(htmlSource);
        log4j.logDebug(htmlSource);
    }
}
