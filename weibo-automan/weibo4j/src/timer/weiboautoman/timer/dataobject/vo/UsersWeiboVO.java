package weiboautoman.timer.dataobject.vo;

public class UsersWeiboVO {

    private Long   id;

    private Long   userId;

    private String weiboId;

    private String weiboEmail;

    private String token;

    private String tokenSecret;

    private String weiboType;

    private String nick;

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
        this.weiboId = weiboId;
    }

    public String getWeiboEmail() {
        return weiboEmail;
    }

    public void setWeiboEmail(String weiboEmail) {
        this.weiboEmail = weiboEmail;
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

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick == null ? null : nick.trim();
    }
}
