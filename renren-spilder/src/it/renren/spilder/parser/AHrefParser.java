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
 * @version 1.0 创建时间：2009-11-11 下午02:52:34 类说明:对传入的网页内容，获取其中的超链接地址
 */
public class AHrefParser {

    private static Log4j log4j = new Log4j(AHrefParser.class.getName());

    /**
     * 获取传入网页内容中所有的链接，以AHrefElement的List返回
     * 
     * @param content 根据指定URL获取的内容
     * @return 所有AHrefElement的对象List
     * @throws ParserException
     */
    public static List<AHrefElement> ahrefParser(String content, String charset) throws ParserException {
        return ahrefParser(content, null, charset);
    }

    /**
     * 获取传入网页内容中所有的链接，以AHrefElement的List返回
     * 
     * @param content 根据指定URL获取的内容
     * @param urlMustInclude 必须包括的字符串
     * @return 所有AHrefElement的对象List
     * @throws ParserException
     */
    public static List<AHrefElement> ahrefParser(String content, String urlMustInclude, String charset)
                                                                                                       throws ParserException {
        return ahrefParser(content, urlMustInclude, null, charset);
    }

    /**
     * 获取传入网页内容中所有的链接，以AHrefElement的List返回
     * 
     * @param content 根据指定URL获取的内容
     * @param urlMustInclude 必须包括的字符串
     * @param urlMustNotInclude 必须不包括的字符串
     * @return 所有AHrefElement的对象List
     * @throws ParserException
     */
    public static List<AHrefElement> ahrefParser(String content, String urlMustInclude, String urlMustNotInclude,
                                                 String charset) throws ParserException {
        return ahrefParser(content, urlMustInclude, urlMustNotInclude, charset, false);
    }

    /**
     * 获取传入网页内容中所有的链接，以AHrefElement的List返回
     * 
     * @param content 根据指定URL获取的内容
     * @param urlMustInclude 必须包括的字符串
     * @param urlMustNotInclude 必须不包括的字符串
     * @param charset 编码
     * @param compByRegex URL中匹配规则是否为正则表达式，如果为否，则通过是否包括字符串来进行比较
     * @return 所有AHrefElement的对象List
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
            if (anode instanceof LinkTag) {// 获取所有的链接
                LinkTag ahrefNode = (LinkTag) anode;
                String href = ahrefNode.getAttribute("HREF");
                href = href == null ? null : href.trim();
                if (ahref.get(href) == null && href != null) {
                    if (compByRegex == true) {/* 通过正则比较 */
                        boolean isMatch = false;
                        if (!StringUtil.isEmpty(urlMustInclude)) {
                            isMatch = must.matcher(href).find();/* 必须包括的字符串的正则，是否符合当前URL */
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
                    } else {/* 通过是否含字符串进行比较 */
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
     * 根据LinkTag返回一个链接标签
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
     * 检测当前的URL是否没有包括不允许包括的字符串
     * 
     * @param href 被检测的URL
     * @param urlMustNotInclude 不允许包含的字符串，可以是多个，以逗号","为分隔符
     * @return 没有包括返回true，有包括返回false
     */
    private static boolean notIncludeMustNotIncludeCheck(String href, String urlMustNotInclude) {
        boolean checkResult = true;
        // 多对外不能够包括的字符进行处理
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
