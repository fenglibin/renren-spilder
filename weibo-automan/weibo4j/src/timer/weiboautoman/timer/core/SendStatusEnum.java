package weiboautoman.timer.core;

/**
 * 类SendStatusEnum.java的实现描述：微博发送状态
 * 
 * @author fenglibin 2011-11-8 下午08:32:27
 */
public enum SendStatusEnum {
    // 依次为:发送失败,发送成功,未发送
    SEND_ERROR("E"), SEND_SUCCESS("Y"), NOT_SEND("N");

    String value = null;

    private SendStatusEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
