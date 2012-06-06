package weiboautoman.timer.job.sender;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.javawind.t_api.beans.OAuth;
import net.javawind.t_api.commons.T_API;
import net.sf.json.JSONObject;

import weiboautoman.timer.core.SendResult;
import weiboautoman.timer.dataobject.vo.UsersTimeMsgVO;
import weiboautoman.timer.util.ImageUtil;
import weiboautoman.timer.util.StringUtil;

/**
 * 类QQWeiboSender.java的实现描述：QQ微博发送实现类
 * 
 * @author fenglibin 2011-10-18 上午08:51:03
 */
public class QQWeiboSender extends WeiboSender {

    private static Logger       log           = LoggerFactory.getLogger(QQWeiboSender.class);
    private static final String appKey        = "801002131";
    private static final String appSecret     = "19f86df9dcd1bacd2885f10c7dfc3bce";
    private static String       callBackURL   = "null";
    private static String       format        = "json";
    private static String       clientIp      = "127.0.0.1";
    /** 腾讯微博的标识 */
    private static final String QQ_WEIBO_MARK = "Q";

    @Override
    public SendResult send(UsersTimeMsgVO msgVO) {
        SendResult result = null;
        String type = super.getWeiboType(msgVO);
        if (type.indexOf(QQ_WEIBO_MARK) < 0) {
            return result;
        }
        if (super.isSendCheck(msgVO)) {
            return result;
        }
        result = new SendResult();
        try {
            OAuth oauth = new OAuth(appKey, appSecret, callBackURL); // 初始化OAuth请求令牌
            oauth.setOauth_token(msgVO.getToken()); // 这里填入Oauth_token
            oauth.setOauth_token_secret(msgVO.getTokenSecret()); // 这里填入oauth_token_secret
            T_API tapi = new T_API();
            String sendResultMessage = null;
            if (!StringUtil.isNull(msgVO.getMsgPicture())) {// 带图片的微博
                String localImage = null;
                if (msgVO.getMsgPicture().startsWith("http")) {// 网络图片
                    localImage = ImageUtil.saveImage(getImagePath(), msgVO.getMsgPicture());
                } else {
                    localImage = getImagePath() + msgVO.getMsgPicture();
                }
                if (!StringUtil.isNull(localImage) && new File(localImage).exists()) {
                    sendResultMessage = tapi.add_pic(oauth, format, msgVO.getMsgContent(), clientIp, localImage);
                } else {
                    result.setReason(result.getReason() + "没有找到图片文件或图片文件处理出错:" + msgVO.getMsgPicture() + ".");
                }
            } else {
                sendResultMessage = tapi.add(oauth, format, msgVO.getMsgContent(), clientIp);
            }
            if (!StringUtil.isNull(sendResultMessage)) {
                JSONObject object = JSONObject.fromObject(sendResultMessage);
                int errcode = object.getInt("errcode");
                int ret = object.getInt("ret");
                String msg = object.getString("msg");
                // 返回码的意思参见：http://open.t.qq.com/resource.php?i=2,2
                if ((errcode == 0 && ret == 0) || (errcode == 13)) {// 13表示重复发表
                    if (errcode == 13) {
                        if (log.isWarnEnabled()) {
                            log.warn("当前微博重复发表，默认为按发布成功处理：" + msgVO.getMsgContent());
                        }
                    }
                    result.setSuccess(true);
                } else {
                    result.setReason(result.getReason() + "腾讯微博发送失败发生异常,errcode:" + errcode + ",ret:" + ret + ",原因："
                                     + msg + ".");
                }
            }

        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("腾讯发送微博失败：" + e.getMessage(), e);
            }
            result.setReason(result.getReason() + "腾讯微博发送失败发生异常,错误信息" + e.getMessage() + ".");
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        OAuth oauth = new OAuth(appKey, appSecret, callBackURL); // 初始化OAuth请求令牌
        oauth.setOauth_token("34308013597d4b4ea21eacc30b910adf"); // 这里填入Oauth_token
        oauth.setOauth_token_secret("9235711588cc8fb972064341f12b6e7e"); // 这里填入oauth_token_secret
        String result = null;
        // // 比如,获取 主页时间线:
        // Statuses_API sapi = new Statuses_API();
        // result = sapi.home_timeline(oauth, "json", "0", "", "20", "", "", "");
        // System.out.println(result);

        T_API tapi = new T_API();
        // String result = tapi.add(oauth, format, "早上好，各位", "12.34.56.78");
        result = tapi.add_pic(oauth, "json", "发一个MM哦", "12.34.56.78", "/usr/fenglibin/images/weibotest/image/9.jpg");
        System.out.println(result);
    }
}
