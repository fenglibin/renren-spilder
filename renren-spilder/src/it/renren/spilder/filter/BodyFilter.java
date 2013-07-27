package it.renren.spilder.filter;

import it.renren.spilder.main.config.ChildPage;
import it.renren.spilder.main.config.ParentPage;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.log.Log4j;

import org.htmlparser.util.ParserException;
import org.jdom.Element;

public class BodyFilter implements Filter {

    private static Log4j log4j = new Log4j(BodyFilter.class.getName());

    @Override
    public String filterContent(ParentPage parentPageConfig, ChildPage childPageConfig, String htmlContent) throws ParserException, RuntimeException {
        return getChildContent(htmlContent, childPageConfig, parentPageConfig);
    }

    /**
     * 针对文章列表中的具体文章内容的形式进行不同的处理，以确定能够最终获取到文章。
     * 
     * @param childBody
     * @param childPageConfig
     * @return
     * @throws RuntimeException
     * @throws ParserException
     */
    private static String getChildContent(String childBody, ChildPage childPageConfig, ParentPage parentPageConfig) throws RuntimeException, ParserException {
        String childContent = "";
        int startSize = childPageConfig.getContent().getStartList().size();
        for (int i = 0; i < startSize; i++) {
            try {
                childContent = StringUtil.subString(childBody, ((Element) childPageConfig.getContent().getStartList().get(i)).getText(),
                                                    ((Element) childPageConfig.getContent().getEndList().get(i)).getText());
                break;
            } catch (Exception e) {
                if (i + 1 == startSize) {
                    throw new RuntimeException(e);
                } else {
                    log4j.logDebug("第 " + (i + 1) + " 次获取文章内容出错！");
                }
            }
        }
        /** 去掉script标签 */
        childContent = StringUtil.removeScript(childContent);
        childContent = StringUtil.replaceContent(childContent, childPageConfig.getContent().getFrom(), childPageConfig.getContent().getTo(), childPageConfig.getContent().isIssRegularExpression());
        return childContent;
    }
}
