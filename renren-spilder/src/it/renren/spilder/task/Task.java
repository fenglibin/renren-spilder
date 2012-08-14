package it.renren.spilder.task;

import it.renren.spilder.main.Constants;
import it.renren.spilder.main.Environment;
import it.renren.spilder.main.config.ChildPage;
import it.renren.spilder.main.config.ParentPage;
import it.renren.spilder.main.detail.ChildPageDetail;
import it.renren.spilder.util.StringUtil;

public abstract class Task {

    protected String getFlag(ParentPage parentPageConfig, ChildPageDetail detail) {
        String flag = "";
        if (detail.isPicArticle()) {
            flag = Constants.ARTICLE_TU;
        }
        if (parentPageConfig.getRandRecommandFrequency() != 0) {
            if (getDealedArticleNum() % parentPageConfig.getRandRecommandFrequency() == 0) {
                if (flag.equals(Constants.ARTICLE_TU)) {
                    flag = flag + "," + Constants.ARTICLE_TUIJIAN;
                } else {
                    flag = Constants.ARTICLE_TUIJIAN;
                }
            }
        }
        if (parentPageConfig.isSRcommand()) {
            flag = flag + "," + Constants.ARTICLE_TEJIAN;
        }
        if (Environment.isImageSite) {
            if (flag.indexOf(Constants.ARTICLE_TOUTIAO) < 0) {
                flag = flag + "," + Constants.ARTICLE_TOUTIAO;
            }
        }
        return flag;
    }

    /**
     * �Ի�ȡ�����ݸ������ý��д���
     * 
     * @param parentPageConfig �б�ҳ�������
     * @param childPageConfig ����ÿ��ҳ�������
     * @param detail ����ÿ��ҳ��ľ������ݣ�����⡢�������
     * @throws Exception
     */
    public abstract void doTask(ParentPage parentPageConfig, ChildPage childPageConfig, ChildPageDetail detail)
                                                                                                               throws Exception;

    protected abstract int getDealedArticleNum();

    /**
     * ��ѯ��ǰURL�Ƿ��Ѿ������
     * 
     * @param url
     * @return
     */
    public abstract boolean isDealed(String url);

    /**
     * ���浱ǰ�ɼ���url
     * 
     * @param url
     */
    public abstract void saveDownUrl(String url);

    /**
     * ����������ԴURL
     * 
     * @param childPageConfig
     * @param detail
     * @param childContent
     * @return
     */
    protected String addSourceUrl(ChildPage childPageConfig, ChildPageDetail detail, String childContent) {
        if (childPageConfig.isAddUrl()) {
            String displayText = childPageConfig.getAddUrlDisplayString();
            if (StringUtil.isEmpty(displayText)) {
                displayText = detail.getUrl();
            }
            childContent = childContent + "<br>From:<a href=\"" + detail.getUrl()
                           + "\" target=\"_blank\" rel=\"external nofollow\">" + displayText + "</a>";
        }
        return childContent;
    }
}
