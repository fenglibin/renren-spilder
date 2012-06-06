package it.renren.spilder.main.config;


/**
 * 单个回复 类Reply.java的实现描述：TODO 类实现描述
 * 
 * @author Administrator 2011-6-5 下午03:41:41
 */
public class Reply extends ContentRangeWithReplace {

    private String start;
    private String end;

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

}
