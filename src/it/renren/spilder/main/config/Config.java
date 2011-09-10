package it.renren.spilder.main.config;

import it.renren.spilder.util.JDomUtil;
import it.renren.spilder.util.StringUtil;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

import bsh.EvalError;

/**
 * 配置初使化 类Config.java的实现描述：TODO 类实现描述
 * 
 * @author Administrator 2011-6-5 下午04:38:04
 */
public class Config {

    public static ParentPage initParentPage(Document ruleXml) throws JDOMException, EvalError {
        ParentPage parentPageConfig = new ParentPage();
        parentPageConfig.setCharset(JDomUtil.getValueByXpath(ruleXml, "/Rules/MainUrl/Charset/Value"));
        parentPageConfig.getUrlListPages().setValues((Element) (XPath.selectSingleNode(ruleXml, "/Rules/MainUrl/Values")));
        parentPageConfig.setImageDescUrl(JDomUtil.getValueByXpath(ruleXml, "/Rules/MainUrl/ImageDescUrl/Value"));
        parentPageConfig.setImageSaveLocation(JDomUtil.getValueByXpath(ruleXml,
                                                                       "/Rules/MainUrl/ImageSaveLocation/Value"));
        parentPageConfig.setRandRecommandFrequency(Integer.parseInt(JDomUtil.getValueByXpath(ruleXml,
                                                                                             "/Rules/MainUrl/Recommend/Value").equals("") ? "0" : JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                                           "/Rules/MainUrl/Recommend/Value")));
        parentPageConfig.setSRcommand(JDomUtil.getValueByXpath(ruleXml, "/Rules/MainUrl/SRecommend/Value") == null ? false : Boolean.parseBoolean(JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                                           "/Rules/MainUrl/SRecommend/Value")));
        parentPageConfig.setFilterDownloadUrl(JDomUtil.getValueByXpath(ruleXml,
                                                                       "/Rules/MainUrl/FilterDownloadUrl/Value") == null ? true : Boolean.parseBoolean(JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                                                "/Rules/MainUrl/FilterDownloadUrl/Value")));

        parentPageConfig.getContent().setStartList(XPath.selectNodes(ruleXml, "/Rules/MainUrl/MainRange/Start/Value"));
        parentPageConfig.getContent().setEndList(XPath.selectNodes(ruleXml, "/Rules/MainUrl/MainRange/End/Value"));

        parentPageConfig.getUrlFilter().setMustInclude(JDomUtil.getValueByXpath(ruleXml,
                                                                                "/Rules/MainUrl/UrlFilter/MustInclude/Value"));
        parentPageConfig.getUrlFilter().setMustNotInclude(JDomUtil.getValueByXpath(ruleXml,
                                                                                   "/Rules/MainUrl/UrlFilter/MustNotInclude/Value"));
        parentPageConfig.getUrlFilter().setCompByRegex(JDomUtil.getValueByXpath(ruleXml,
                                                                                "/Rules/MainUrl/UrlFilter/IsCompByRegex/Value") == null ? false : Boolean.parseBoolean(JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                                                                "/Rules/MainUrl/UrlFilter/IsCompByRegex/Value")));
        parentPageConfig.setDesArticleId(JDomUtil.getValueByXpath(ruleXml, "/Rules/MainUrl/DesArticleId/Value"));
        parentPageConfig.setAutoDetectTypeMapClass(JDomUtil.getValueByXpath(ruleXml,
                                                                            "/Rules/MainUrl/AutoDetect/TypeMapMakeClass") == null ? "" : JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                                  "/Rules/MainUrl/AutoDetect/TypeMapMakeClass"));
        parentPageConfig.setOneUrlSleepTime(JDomUtil.getValueByXpath(ruleXml, "/Rules/MainUrl/OneUrlSleepTime/Value") == null ? 0 : Long.parseLong(JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                                            "/Rules/MainUrl/OneUrlSleepTime/Value")));
        parentPageConfig.getTranslater().setFrom(JDomUtil.getValueByXpath(ruleXml,
                                                                          "/Rules/MainUrl/Translater/From/Value") == null ? "" : JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                          "/Rules/MainUrl/Translater/From/Value"));
        parentPageConfig.getTranslater().setTo(JDomUtil.getValueByXpath(ruleXml, "/Rules/MainUrl/Translater/To/Value") == null ? "" : JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                               "/Rules/MainUrl/Translater/To/Value"));
        parentPageConfig.setBlogType(Integer.parseInt(JDomUtil.getValueByXpath(ruleXml,
                                                                               "/Rules/MainUrl/BlogType/TypeId") == null ? "0" : JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                          "/Rules/MainUrl/BlogType/TypeId")));
        parentPageConfig.setHomeUrlAddStr(JDomUtil.getValueByXpath(ruleXml, "/Rules/MainUrl/BlogType/HomeUrlAddStr"));
        return parentPageConfig;
    }

    public static ChildPage initChildPage(Document ruleXml) throws JDOMException {
        ChildPage childPageConfig = new ChildPage();
        childPageConfig.setCharset(JDomUtil.getValueByXpath(ruleXml, "/Rules/Child/Charset/Value"));
        childPageConfig.getTitle().setStart(JDomUtil.getValueByXpathNotTrim(ruleXml, "/Rules/Child/Title/Start/Value"));
        childPageConfig.getTitle().setEnd(JDomUtil.getValueByXpathNotTrim(ruleXml, "/Rules/Child/Title/End/Value"));
        childPageConfig.getTitle().setIssRegularExpression(Boolean.parseBoolean(JDomUtil.getValueByXpath(ruleXml,
                                                                                                         "/Rules/Child/Title/Replace/IsRegularExpression/Value")));
        childPageConfig.getTitle().setFrom(JDomUtil.getValueByXpath(ruleXml, "/Rules/Child/Title/Replace/From/Value"));
        childPageConfig.getTitle().setFrom(JDomUtil.getValueByXpath(ruleXml, "/Rules/Child/Title/Replace/To/Value"));

        childPageConfig.getContent().setStartList(XPath.selectNodes(ruleXml, "/Rules/Child/Content/Start/Value"));
        childPageConfig.getContent().setEndList(XPath.selectNodes(ruleXml, "/Rules/Child/Content/End/Value"));

        childPageConfig.getContent().setIssRegularExpression(Boolean.parseBoolean(JDomUtil.getValueByXpath(ruleXml,
                                                                                                           "/Rules/Child/Content/Replace/IsRegularExpression/Value")));
        childPageConfig.getContent().setFrom(JDomUtil.getValueByXpath(ruleXml,
                                                                      "/Rules/Child/Content/Replace/From/Value"));
        childPageConfig.getContent().setTo(JDomUtil.getValueByXpath(ruleXml, "/Rules/Child/Content/Replace/To/Value"));
        childPageConfig.getContent().setWashContent(JDomUtil.getValueByXpath(ruleXml,
                                                                             "/Rules/Child/Content/WashContent/Value") == null ? "" : JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                               "/Rules/Child/Content/WashContent/Value"));
        childPageConfig.getContent().setHandler(JDomUtil.getValueByXpath(ruleXml, "/Rules/Child/Content/Handler/Value") == null ? "" : JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                                "/Rules/Child/Content/Handler/Value"));
        /** 如果存在着对回复节点的处理 */
        if (JDomUtil.getValueByXpath(ruleXml, "/Rules/Child/Content/Replys/Start/Value") != null) {
            childPageConfig.getReplys().setStart(StringUtil.returnBlankIfNull(JDomUtil.getValueByXpath(ruleXml,
                                                                                                       "/Rules/Child/Content/Replys/Start/Value")));
            childPageConfig.getReplys().setEnd(StringUtil.returnBlankIfNull(JDomUtil.getValueByXpath(ruleXml,
                                                                                                     "/Rules/Child/Content/Replys/End/Value")));
            childPageConfig.getReplys().setFirstMainContent(StringUtil.returnFalseIfNull(JDomUtil.getValueByXpath(ruleXml,
                                                                                                                  "/Rules/Child/Content/Replys/IsFirstMainContent/Value")));
            childPageConfig.getReplys().setIssRegularExpression(StringUtil.returnFalseIfNull(JDomUtil.getValueByXpath(ruleXml,
                                                                                                                      "/Rules/Child/Content/Replys/Replace/IsRegularExpression/Value")));
            childPageConfig.getReplys().setFrom(StringUtil.returnBlankIfNull(JDomUtil.getValueByXpath(ruleXml,
                                                                                                      "/Rules/Child/Content/Replys/Replace/From/Value")));
            childPageConfig.getReplys().setTo(StringUtil.returnBlankIfNull(JDomUtil.getValueByXpath(ruleXml,
                                                                                                    "/Rules/Child/Content/Replys/Replace/To/Value")));
            childPageConfig.getReplys().getReply().setStart(StringUtil.returnBlankIfNull(JDomUtil.getValueByXpath(ruleXml,
                                                                                                                  "/Rules/Child/Content/Replys/Reply/Start/Value")));
            childPageConfig.getReplys().getReply().setEnd(StringUtil.returnBlankIfNull(JDomUtil.getValueByXpath(ruleXml,
                                                                                                                "/Rules/Child/Content/Replys/Reply/End/Value")));
            childPageConfig.getReplys().getReply().setIssRegularExpression(StringUtil.returnFalseIfNull(JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                 "/Rules/Child/Content/Replys/Reply/Replace/IsRegularExpression/Value")));
            childPageConfig.getReplys().getReply().setFrom(StringUtil.returnBlankIfNull(JDomUtil.getValueByXpath(ruleXml,
                                                                                                                 "/Rules/Child/Content/Replys/Replace/Reply/From/Value")));
            childPageConfig.getReplys().getReply().setTo(StringUtil.returnBlankIfNull(JDomUtil.getValueByXpath(ruleXml,
                                                                                                               "/Rules/Child/Content/Replys/Reply/Replace/To/Value")));
        }
        childPageConfig.setAddUrl(JDomUtil.getValueByXpath(ruleXml, "/Rules/Child/AddUrl/Value") == null ? false : Boolean.parseBoolean(JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                                 "/Rules/Child/AddUrl/Value")));
        childPageConfig.setAddUrlDisplayString(JDomUtil.getValueByXpath(ruleXml, "/Rules/Child/AddUrl/Display") == null ? "" : JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                        "/Rules/Child/AddUrl/Display"));
        childPageConfig.setKeepFileName(JDomUtil.getValueByXpath(ruleXml, "/Rules/Child/KeepFileName/Value") == null ? false : Boolean.parseBoolean(JDomUtil.getValueByXpath(ruleXml,
                                                                                                                                                                             "/Rules/Child/KeepFileName/Value")));
        return childPageConfig;
    }
}
