package it.renren.spilder.task;

import java.sql.SQLException;

import it.renren.spilder.main.Constants;
import it.renren.spilder.main.config.ChildPage;
import it.renren.spilder.main.config.ParentPage;
import it.renren.spilder.main.detail.ChildPageDetail;

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
        return flag;
    }

    /* �����Ѿ���ȡ���ݵ�URL�����������������ظ����쳣��˵����URL�Ѿ���ȡ�����ݣ�Ϊ�������� */
    protected abstract void saveDownUrl(ParentPage parentPageConfig, ChildPageDetail detail) throws SQLException;

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
}
