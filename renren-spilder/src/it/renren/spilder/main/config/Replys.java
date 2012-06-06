package it.renren.spilder.main.config;

/**
 * 所有回复 类Replys.java的实现描述：TODO 类实现描述
 * 
 * @author Administrator 2011-6-5 下午03:41:53
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
