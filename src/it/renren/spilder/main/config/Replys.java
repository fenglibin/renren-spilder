package it.renren.spilder.main.config;

/**
 * ���лظ� ��Replys.java��ʵ��������TODO ��ʵ������
 * 
 * @author Administrator 2011-6-5 ����03:41:53
 */
public class Replys extends ContentRangeWithReplace {

    private String start;
    private String end;
    private Reply  reply = new Reply();

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Reply getReply() {
        return reply;
    }

}
