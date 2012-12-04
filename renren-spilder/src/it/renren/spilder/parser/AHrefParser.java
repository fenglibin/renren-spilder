package it.renren.spilder.parser;

import it.renren.spilder.main.Constants;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.UrlUtil;
import it.renren.spilder.util.log.Log4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * @author Fenglibin
 * @E-mail:56553655@163.com
 * @blog :blog.csdn.net/fenglibing
 * @site :www.6666-6666.com
 * @version 1.0 ����ʱ�䣺2009-11-11 ����02:52:34 ��˵��:�Դ������ҳ���ݣ���ȡ���еĳ����ӵ�ַ
 */
public class AHrefParser {

    private static Log4j log4j = new Log4j(AHrefParser.class.getName());

    /**
     * ��ȡ������ҳ���������е����ӣ���AHrefElement��List����
     * 
     * @param content ����ָ��URL��ȡ������
     * @return ����AHrefElement�Ķ���List
     * @throws ParserException
     */
    public static List<AHrefElement> ahrefParser(String content, String charset) throws ParserException {
        return ahrefParser(content, null, charset);
    }

    /**
     * ��ȡ������ҳ���������е����ӣ���AHrefElement��List����
     * 
     * @param content ����ָ��URL��ȡ������
     * @param urlMustInclude ����������ַ���
     * @return ����AHrefElement�Ķ���List
     * @throws ParserException
     */
    public static List<AHrefElement> ahrefParser(String content, String urlMustInclude, String charset)
                                                                                                       throws ParserException {
        return ahrefParser(content, urlMustInclude, null, charset);
    }

    /**
     * ��ȡ������ҳ���������е����ӣ���AHrefElement��List����
     * 
     * @param content ����ָ��URL��ȡ������
     * @param urlMustInclude ����������ַ���
     * @param urlMustNotInclude ���벻�������ַ���
     * @return ����AHrefElement�Ķ���List
     * @throws ParserException
     */
    public static List<AHrefElement> ahrefParser(String content, String urlMustInclude, String urlMustNotInclude,
                                                 String charset) throws ParserException {
        return ahrefParser(content, urlMustInclude, urlMustNotInclude, charset, false);
    }

    /**
     * ��ȡ������ҳ���������е����ӣ���AHrefElement��List����
     * 
     * @param content ����ָ��URL��ȡ������
     * @param urlMustInclude ����������ַ���
     * @param urlMustNotInclude ���벻�������ַ���
     * @param charset ����
     * @param compByRegex URL��ƥ������Ƿ�Ϊ������ʽ�����Ϊ����ͨ���Ƿ�����ַ��������бȽ�
     * @return ����AHrefElement�Ķ���List
     * @throws ParserException
     */
    public static List<AHrefElement> ahrefParser(String content, String urlMustInclude, String urlMustNotInclude,
                                                 String charset, boolean compByRegex) throws ParserException {
        List<AHrefElement> ret = new ArrayList<AHrefElement>();
        Parser myParser = null;
        NodeList nodeList = null;
        myParser = Parser.createParser(content, charset);
        NodeFilter hrefFilter = new NodeClassFilter(LinkTag.class);
        OrFilter lastFilter = new OrFilter();
        lastFilter.setPredicates(new NodeFilter[] { hrefFilter });
        Map<String, String> ahref = new HashMap<String, String>();

        nodeList = myParser.parse(lastFilter);
        Node[] nodes = nodeList.toNodeArray();
        Pattern must = null;
        if (!StringUtil.isEmpty(urlMustInclude) && compByRegex) {
            must = Pattern.compile(urlMustInclude);
        }
        Pattern mustnot = null;
        if (!StringUtil.isEmpty(urlMustNotInclude) && compByRegex) {
            mustnot = Pattern.compile(urlMustNotInclude);
        }
        for (int i = 0; i < nodes.length; i++) {
            Node anode = (Node) nodes[i];
            if (anode instanceof LinkTag) {// ��ȡ���е�����
                LinkTag ahrefNode = (LinkTag) anode;
                String href = ahrefNode.getAttribute("HREF");
                href = href == null ? null : href.trim();
                if (ahref.get(href) == null && href != null) {
                    if (compByRegex == true) {/* ͨ������Ƚ� */
                        boolean isMatch = false;
                        if (!StringUtil.isEmpty(urlMustInclude)) {
                            isMatch = must.matcher(href).find();/* ����������ַ����������Ƿ���ϵ�ǰURL */
                            if (isMatch == true) {
                                if (!StringUtil.isEmpty(urlMustNotInclude)) {
                                    isMatch = mustnot.matcher(urlMustNotInclude).find();
                                    if (!isMatch) {
                                        ret.add(newAHrefElement(ahrefNode));
                                    }
                                } else {
                                    ret.add(newAHrefElement(ahrefNode));
                                }
                            }
                        } else {
                            if (!StringUtil.isEmpty(urlMustNotInclude)) {
                                isMatch = mustnot.matcher(urlMustNotInclude).find();
                                if (!isMatch) {
                                    ret.add(newAHrefElement(ahrefNode));
                                }
                            } else {
                                ret.add(newAHrefElement(ahrefNode));
                            }
                        }
                    } else {/* ͨ���Ƿ��ַ������бȽ� */
                        if (!StringUtil.isEmpty(urlMustInclude)) {
                            if (href.indexOf(urlMustInclude) >= 0) {
                                if (!StringUtil.isEmpty(urlMustNotInclude)) {
                                    if (notIncludeMustNotIncludeCheck(href, urlMustNotInclude)) {
                                        ret.add(newAHrefElement(ahrefNode));
                                    }
                                } else {
                                    ret.add(newAHrefElement(ahrefNode));
                                }
                            }
                        } else {
                            if (!StringUtil.isEmpty(urlMustNotInclude)) {
                                if (notIncludeMustNotIncludeCheck(href, urlMustNotInclude)) {
                                    ret.add(newAHrefElement(ahrefNode));
                                }
                            } else {
                                ret.add(newAHrefElement(ahrefNode));
                            }
                        }
                    }
                    ahref.put(href, href);
                }
            }
        }
        return ret;
    }

    /**
     * ����LinkTag����һ�����ӱ�ǩ
     * 
     * @param linkTag
     * @return
     */
    private static AHrefElement newAHrefElement(LinkTag linkTag) {
        AHrefElement fe = new AHrefElement();
        fe.setHref(linkTag.getAttribute("HREF").trim());
        fe.setHrefText(linkTag.getLinkText().trim());
        return fe;
    }

    /**
     * ��⵱ǰ��URL�Ƿ�û�а���������������ַ���
     * 
     * @param href ������URL
     * @param urlMustNotInclude ������������ַ����������Ƕ�����Զ���","Ϊ�ָ���
     * @return û�а�������true���а�������false
     */
    private static boolean notIncludeMustNotIncludeCheck(String href, String urlMustNotInclude) {
        boolean checkResult = true;
        // ����ⲻ�ܹ��������ַ����д���
        String[] urlMustNotIncludeArray = urlMustNotInclude.split(Constants.COMMA);
        for (String oneMustNotInclude : urlMustNotIncludeArray) {
            if (href.indexOf(oneMustNotInclude) > 0) {
                checkResult = Boolean.FALSE;
                break;
            }
        }
        return checkResult;
    }

    public static void main(String[] args) throws IOException, ParserException {
        String url = "http://www.searchdatabase.com.cn/articlelist-1-652-31.htm";
        String content = UrlUtil.getContentByURL(url);
        List<AHrefElement> list = ahrefParser(content, null, "gbk");
        for (AHrefElement ahref : list) {
            log4j.logDebug(ahref.getHref());
        }
    }
}
