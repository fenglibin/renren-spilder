package weiboautoman.timer.job.sender;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import weiboautoman.timer.core.Constants;
import weiboautoman.timer.core.SendResult;
import weiboautoman.timer.dataobject.vo.UsersTimeMsgVO;
import weiboautoman.timer.util.ImageUtil;
import weiboautoman.timer.util.StringUtil;

/**
 * 类SinaWeiboSender.java的实现描述：新浪微博发送实现类
 * 
 * @author fenglibin 2011-10-18 上午08:51:24
 */
public class SinaWeiboSender extends WeiboSender {

    private static Logger       log             = LoggerFactory.getLogger(SinaWeiboSender.class);
    /** 新浪微博的标识 */
    private static final String SINA_WEIBO_MARK = "S";

    @Override
    public SendResult send(UsersTimeMsgVO msgVO) {
        SendResult result = null;
        String type = super.getWeiboType(msgVO);
        if (type.indexOf(SINA_WEIBO_MARK) < 0) {
            return result;
        }
        if (super.isSendCheck(msgVO)) {
            return result;
        }
        result = new SendResult();
        Status status = null;
        try {
            Weibo weibo = new Weibo();
            weibo.setToken(msgVO.getToken(), msgVO.getTokenSecret());
            if (!StringUtil.isNull(msgVO.getMsgPicture())) {// 带图片的微博
                String localImage = null;
                if (msgVO.getMsgPicture().startsWith("http")) {// 网络图片
                    localImage = ImageUtil.saveImage(getImagePath(), msgVO.getMsgPicture());
                } else {
                    localImage = getImagePath() + msgVO.getMsgPicture();
                }
                if (!StringUtil.isNull(localImage) && new File(localImage).exists()) {
                    status = weibo.uploadStatus(msgVO.getMsgContent(), new File(localImage));
                } else {
                    result.setReason(result.getReason() + "没有找到图片文件或图片文件处理出错:" + msgVO.getMsgPicture() + ".");
                }
            } else {
                status = weibo.updateStatus(msgVO.getMsgContent());
            }
            if (status != null) {
                if (status.getResponse().getStatusCode() == Constants.HTTP_OK_RESPONSE) {
                    result.setSuccess(Boolean.TRUE);
                } else if (status.getResponse().getStatusCode() == Constants.REPEAT_MESSAGE_ERR_CODE
                           && status.getResponse().getResponseAsString().indexOf("40028:不要太贪心哦！你已经发过一次啦") > 0) {
                    if (log.isWarnEnabled()) {
                        log.warn("当前微博重复发表，默认为按发布成功处理：" + msgVO.getMsgContent());
                    }
                    result.setSuccess(Boolean.TRUE);
                } else {
                    result.setReason(result.getReason() + "新浪微博发送失败,返回状态码:" + status.getResponse().getStatusCode()
                                     + ".");
                }
            }
        } catch (WeiboException e) {
            if (log.isErrorEnabled()) {
                log.error("新浪发送微博失败1：" + e.getMessage(), e);
            }
            if (e.getStatusCode() == Constants.REPEAT_MESSAGE_ERR_CODE
                && e.getMessage().indexOf("repeated weibo text") > 0) {
                result.setSuccess(Boolean.TRUE);
            } else {
                result.setReason(result.getReason() + "新浪发送微博失败发生异常,错误码:" + e.getStatusCode() + ",错误信息"
                                 + e.getMessage() + ".");
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("新浪发送微博失败2：" + e.getMessage(), e);
            }
            result.setReason(result.getReason()
                             + "新浪发送微博失败发生异常,"
                             + (status != null ? "状态码:" + status.getResponse().getStatusCode() : "错误信息"
                                                                                                 + e.getMessage())
                             + ".");
        }
        return result;
    }
}
