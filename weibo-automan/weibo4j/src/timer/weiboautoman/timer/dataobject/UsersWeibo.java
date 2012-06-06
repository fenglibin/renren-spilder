package weiboautoman.timer.dataobject;

public class UsersWeibo {
    private Long id;

    private Long userId;

    private String weiboId;

    private String weiboEmail;

    private String token;

    private String tokenSecret;

    private String weiboType;

    private String nick;

    private String location;

    private String head;

    private String introduction;

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

    public String getWeiboEmail() {
        return weiboEmail;
    }

    public void setWeiboEmail(String weiboEmail) {
        this.weiboEmail = weiboEmail == null ? null : weiboEmail.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret == null ? null : tokenSecret.trim();
    }

    public String getWeiboType() {
        return weiboType;
    }

    public void setWeiboType(String weiboType) {
        this.weiboType = weiboType == null ? null : weiboType.trim();
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick == null ? null : nick.trim();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head == null ? null : head.trim();
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
    }
}