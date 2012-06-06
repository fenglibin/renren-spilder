package weiboautoman.timer.core;

/**
 * 类SendResult.java的实现描述：微博发送结果封装类
 * 
 * @author fenglibin 2011-11-9 下午02:14:53
 */
public class SendResult {

    boolean success;
    String  reason = "";

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
