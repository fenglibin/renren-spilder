package it.renren.spilder.filter;

import it.renren.spilder.main.config.ChildPage;
import it.renren.spilder.main.config.ParentPage;
import it.renren.spilder.util.StringUtil;

/**
 * 从给定的内容中获取标题。
 * 
 * @author Administrator 2012-8-2 下午08:57:49
 */
public class TitleFilter implements Filter {

    @Override
    public String filterContent(ParentPage parentPageConfig, ChildPage childPageConfig, String htmlContent) {
        String title = null;
        if (StringUtil.isEmpty(childPageConfig.getTitle().getStart())) {
            return title;
        }
        title = StringUtil.subString(htmlContent, childPageConfig.getTitle().getStart(), childPageConfig.getTitle().getEnd());
        title = StringUtil.removeHtmlTags(title);
        title = replaceTitle(childPageConfig, title);
        return title;
    }

    /* 标题替换 */
    private static String replaceTitle(ChildPage childPageConfig, String childTitle) {
        if (!StringUtil.isEmpty(childPageConfig.getTitle().getFrom()) && !StringUtil.isEmpty(childPageConfig.getTitle().getTo())) {
            if (childPageConfig.getTitle().isIssRegularExpression()) {
                childTitle = childTitle.replaceAll(childPageConfig.getTitle().getFrom(), childPageConfig.getTitle().getTo());
            } else {
                childTitle = childTitle.replace(childPageConfig.getTitle().getFrom(), childPageConfig.getTitle().getTo());
            }
        }
        return childTitle;
    }

}
