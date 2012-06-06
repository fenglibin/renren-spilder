package weiboautoman.timer.dataobject.vo;

public class TimeMsgWeiboId {

    /* 微博类型:S表示新浪微博、Q表示腾讯的微博 */
    private String  type;
    /* 对应表users_weibo的主键id字段 */
    private long    uwid;
    /* 如果发送失败,这里记录失败原因 */
    private String  reason;
    /* 用户的呢称 */
    private String  nick;
    /* 发送结果 */
    private boolean result;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getUwid() {
        return uwid;
    }

    public void setUwid(long uwid) {
        this.uwid = uwid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

}
