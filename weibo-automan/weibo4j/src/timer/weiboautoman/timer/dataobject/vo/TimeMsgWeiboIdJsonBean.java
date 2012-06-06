package weiboautoman.timer.dataobject.vo;

public class TimeMsgWeiboIdJsonBean {

    boolean          result = Boolean.TRUE;
    TimeMsgWeiboId[] timeMsgWeiboId;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public TimeMsgWeiboId[] getTimeMsgWeiboId() {
        return timeMsgWeiboId;
    }

    public void setTimeMsgWeiboId(TimeMsgWeiboId[] timeMsgWeiboId) {
        this.timeMsgWeiboId = timeMsgWeiboId;
    }

}
