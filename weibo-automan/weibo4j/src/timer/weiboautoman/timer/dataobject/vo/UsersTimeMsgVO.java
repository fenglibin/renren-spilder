package weiboautoman.timer.dataobject.vo;

public class UsersTimeMsgVO {

    private Long    id;

    private Long    userId;

    private String  weiboId;

    private String  msgContent;

    private String  msgPicture;

    private Integer sendTime;

    private String  isSend;

    private String  token;

    private String  tokenSecret;

    private String  weiboType;

    private String  sendResult;

    private Long    userWeiboId;

    private String  nick;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(String weiboId) {
        this.weiboId = weiboId == null ? null : weiboId.trim();
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent == null ? null : msgContent.trim();
    }

    public String getMsgPicture() {
        return msgPicture;
    }

    public void setMsgPicture(String msgPicture) {
        this.msgPicture = msgPicture == null ? null : msgPicture.trim();
    }

    public Integer getSendTime() {
        return sendTime;
    }

    public void setSendTime(Integer sendTime) {
        this.sendTime = sendTime;
    }

    public String getIsSend() {
        return isSend;
    }

    public void setIsSend(String isSend) {
        this.isSend = isSend == null ? null : isSend.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public String getWeiboType() {
        return weiboType;
    }

    public void setWeiboType(String weiboType) {
        this.weiboType = weiboType;
    }

    public String getSendResult() {
        return sendResult;
    }

    public void setSendResult(String sendResult) {
        this.sendResult = sendResult;
    }

    public Long getUserWeiboId() {
        return userWeiboId;
    }

    public void setUserWeiboId(Long userWeiboId) {
        this.userWeiboId = userWeiboId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick == null ? null : nick.trim();
    }

    public String toString() {
        return new StringBuilder("id:" + id).append("userId:" + userId).append("weiboId:" + weiboId).append("msgContent:"
                                                                                                                    + msgContent).append("msgPicture:"
                                                                                                                                                 + msgPicture).append("sendTime:"
                                                                                                                                                                              + sendTime).append("isSend:"
                                                                                                                                                                                                         + isSend).append("token:"
                                                                                                                                                                                                                                  + token).append("tokenSecret:"
                                                                                                                                                                                                                                                          + tokenSecret).append("weiboType:"
                                                                                                                                                                                                                                                                                        + weiboType).append("userWeiboId:"
                                                                                                                                                                                                                                                                                                                    + userWeiboId).append("nick:"
                                                                                                                                                                                                                                                                                                                                                  + nick).append("sendResult:"
                                                                                                                                                                                                                                                                                                                                                                         + sendResult).toString();
    }
}
