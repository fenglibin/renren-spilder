package it.renren.spilder.main.config;

/**
 * ���лظ� ��Replys.java��ʵ��������TODO ��ʵ������
 * 
 * @author Administrator 2011-6-5 ����03:41:53
 */
public class Replys extends ContentRangeWithReplace {

    private boolean isFirstMainContent;
    private Reply   reply = new Reply();


    public boolean isFirstMainContent() {
        return isFirstMainContent;
    }

    public void setFirstMainContent(boolean isFirstMainContent) {
        this.isFirstMainContent = isFirstMainContent;
    }

    public Reply getReply() {
        return reply;
    }

}
