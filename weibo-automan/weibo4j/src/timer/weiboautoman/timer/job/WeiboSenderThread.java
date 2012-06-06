package weiboautoman.timer.job;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weibo4j.WeiboException;
import weiboautoman.timer.bo.UsersTimeMsgBO;
import weiboautoman.timer.core.Constants;
import weiboautoman.timer.core.SendResult;
import weiboautoman.timer.core.SendStatusEnum;
import weiboautoman.timer.dao.UsersTimeMsgDAO;
import weiboautoman.timer.dao.UsersWeiboDAO;
import weiboautoman.timer.dataobject.UsersTimeMsg;
import weiboautoman.timer.dataobject.vo.TimeMsgWeiboId;
import weiboautoman.timer.dataobject.vo.TimeMsgWeiboIdJsonBean;
import weiboautoman.timer.dataobject.vo.UsersTimeMsgVO;
import weiboautoman.timer.dataobject.vo.UsersWeiboVO;
import weiboautoman.timer.job.sender.WeiboSender;
import weiboautoman.timer.util.NumberUtil;
import weiboautoman.timer.util.StringUtil;

/**
 * 类WeiboSenderThread.java的实现描述：发送微博的线程
 * 
 * @author fenglibin 2011-10-6 下午03:00:18
 */
public class WeiboSenderThread implements Runnable, Cloneable {

    private static Logger            log      = LoggerFactory.getLogger(WeiboSenderThread.class);
    int                              userIdFirstNumber;
    private UsersTimeMsgDAO          usersTimeMsgDAO;
    private UsersTimeMsgBO           usersTimeMsgBO;
    private UsersWeiboDAO            usersWeiboDAO;
    private String                   imagePath;
    private Map<String, WeiboSender> weiboSender;
    /* 默认获取定时发送的微博 */
    private String                   sendType = "N";

    @Override
    public void run() {
        try {
            long totalCount = usersTimeMsgDAO.selectByUserIdFirstNumberLikeCount(String.valueOf(userIdFirstNumber),
                                                                                 sendType);
            if (totalCount <= 0) {
                if (log.isDebugEnabled()) {
                    log.debug("totalCount is :" + totalCount);
                }
                return;
            }
            long pages = NumberUtil.getPages(totalCount, Constants.DEFAULT_PAGE_SIZE);
            if (log.isDebugEnabled()) {
                log.debug("userIdFirstNumber:" + userIdFirstNumber + " get totalCount:" + totalCount + " ,pages:"
                          + pages);
            }
            for (long currentPage = 1; currentPage <= pages; currentPage++) {
                long start = (currentPage - 1) * Constants.DEFAULT_PAGE_SIZE;
                List<UsersTimeMsg> timeWeiboList = usersTimeMsgDAO.selectByUserIdFirstNumberLike(String.valueOf(userIdFirstNumber),
                                                                                                 sendType,
                                                                                                 start,
                                                                                                 Constants.DEFAULT_PAGE_SIZE);
                if (timeWeiboList != null && timeWeiboList.size() > 0) {
                    for (UsersTimeMsg msgDO : timeWeiboList) {
                        /* 待发送的Weibo类型 */
                        TimeMsgWeiboIdJsonBean weiboIdJsonBean = getWeiboIdJsonBean(msgDO.getWeiboId());
                        if (weiboIdJsonBean == null) {
                            if (log.isWarnEnabled()) {
                                log.warn("根据Json字符串，未能够映射为待发送微博的Java对象：" + msgDO.getWeiboId());
                            }
                            continue;
                        }
                        weiboIdJsonBean.setResult(true);
                        /* 已经发送失败的微博类型 */
                        TimeMsgWeiboIdJsonBean errSendWeiboIdJsonBean = null;
                        /*
                         * 当前消息是否已经发送，Y表示已经发送(且发送成功)，N表示未发送，E表示发送时有错误，默认值为N.
                         * 此处发送消息时,需要判断is_send,如果为N,则全部微博类型直接发送即可,如果为E,则需要从SEND_RESULT字段中读取是哪些微博发送失败了, 只需要发送原来发送失败的记录即可.
                         */
                        errSendWeiboIdJsonBean = getWeiboIdJsonBean(msgDO.getSendResult());
                        if (weiboIdJsonBean != null && weiboIdJsonBean.getTimeMsgWeiboId().length > 0) {
                            for (TimeMsgWeiboId timeMsgWeiboId : weiboIdJsonBean.getTimeMsgWeiboId()) {
                                UsersWeiboVO usersWeiboVO = usersWeiboDAO.selectByPrimaryKeySmall(timeMsgWeiboId.getUwid());
                                /* 如果用户已经取消息了该微博的绑定，则不发送了 */
                                if (usersWeiboVO == null) {
                                    continue;
                                }
                                if (errSendWeiboIdJsonBean != null) {/* 有发送失败的类型 */
                                    /* 检测当前类型是否是发送失败的类型,不是就说明是已经发送成功了的,就不发送,继续找下面的 */
                                    if (!checkIsErrSendType(errSendWeiboIdJsonBean, usersWeiboVO.getId())) {
                                        continue;
                                    }
                                }
                                UsersTimeMsgVO msgVO = new UsersTimeMsgVO();
                                msgVO.setId(msgDO.getId());
                                msgVO.setMsgPicture(msgDO.getMsgPicture());
                                msgVO.setMsgContent(msgDO.getMsgContent());
                                msgVO.setUserWeiboId(usersWeiboVO.getId());
                                msgVO.setNick(usersWeiboVO.getNick());
                                msgVO.setToken(usersWeiboVO.getToken());
                                msgVO.setTokenSecret(usersWeiboVO.getTokenSecret());
                                msgVO.setWeiboType(usersWeiboVO.getWeiboType());
                                if (SendStatusEnum.SEND_SUCCESS.getValue().equals(msgDO.getIsSend())) {
                                    continue;
                                }
                                sendWeibo(weiboIdJsonBean, msgVO);
                            }
                            String sendResultText = null;
                            if (!weiboIdJsonBean.isResult()) {/* 有发送失败的 */
                                sendResultText = JSONObject.fromObject(weiboIdJsonBean).toString();
                                if (sendResultText.length() > Constants.MAX_ERROR_MESSAGE_LENGTH) {
                                    sendResultText = sendResultText.substring(0, Constants.MAX_ERROR_MESSAGE_LENGTH);
                                }
                                if (log.isWarnEnabled()) {
                                    log.warn("users_time_msg id::" + msgDO.getId() + ",user_id:" + msgDO.getUserId()
                                             + " send weibo cause " + " some error. the error sendResult:"
                                             + sendResultText);
                                }
                            }
                            usersTimeMsgBO.updateUserTimeMsgSendStatusAndResult(msgDO.getId(), sendResultText);
                        }

                    }
                }
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("发送微博发生异常：" + e.getMessage(), e);
            }
        }
    }

    /**
     * 发送一条微博，在发送成功后，更新当前记录的状态为已发送
     * 
     * @param msgVO
     */
    private void sendWeibo(TimeMsgWeiboIdJsonBean weiboIdJsonBean, UsersTimeMsgVO msgVO) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("start send weibo to token:" + msgVO.getToken() + ",token secret:" + msgVO.getTokenSecret());
            }
            doSend(weiboIdJsonBean, msgVO);
        } catch (WeiboException e) {
            if (log.isErrorEnabled()) {
                log.error("发送微博发生异常1:" + msgVO.toString(), e);
            }
            weiboIdJsonBean.setResult(Boolean.FALSE);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("发送微博发生异常2:" + msgVO.toString(), e);
            }
            weiboIdJsonBean.setResult(Boolean.FALSE);
        }
    }

    /**
     * 执行发送的动作。如果涉及到多个同条消息要发送到多个微博，只要有一个发送失败了，就认为是发送失败了
     * 
     * @param msgVO
     * @return 返回微博发送的结果
     * @throws WeiboException
     */
    private void doSend(TimeMsgWeiboIdJsonBean weiboIdJsonBean, UsersTimeMsgVO msgVO) throws WeiboException {
        WeiboSender sender = weiboSender.get(msgVO.getWeiboType().toUpperCase());
        if (sender == null) {
            if (log.isWarnEnabled()) {
                log.warn("can not get WeiboSender by weibo type:" + msgVO.getWeiboType());
            }
            return;
        }
        sender.setImagePath(imagePath);
        SendResult result = sender.send(msgVO);
        if (result != null && !result.isSuccess()) {/* 没有发送成功 */
            weiboIdJsonBean.setResult(Boolean.FALSE);
            for (TimeMsgWeiboId timeMsgWeiboId : weiboIdJsonBean.getTimeMsgWeiboId()) {// 保留当前发送的错误信息
                if (timeMsgWeiboId.getUwid() == msgVO.getUserWeiboId()) {
                    timeMsgWeiboId.setNick(msgVO.getNick());
                    timeMsgWeiboId.setReason(result.getReason());
                    break;
                }
            }
        } else if (result != null && result.isSuccess()) {/* 发送成功 */
            for (TimeMsgWeiboId timeMsgWeiboId : weiboIdJsonBean.getTimeMsgWeiboId()) {// 标记发送成功的类型为成功状态
                if (timeMsgWeiboId.getUwid() == msgVO.getUserWeiboId()) {
                    timeMsgWeiboId.setResult(true);
                    break;
                }
            }
        }
    }

    public WeiboSenderThread clone() {
        WeiboSenderThread thread = new WeiboSenderThread();
        thread.usersTimeMsgBO = usersTimeMsgBO;
        thread.usersTimeMsgDAO = usersTimeMsgDAO;
        thread.imagePath = imagePath;
        thread.weiboSender = weiboSender;
        thread.usersWeiboDAO = usersWeiboDAO;
        thread.sendType = sendType;
        return thread;
    }

    /**
     * 将当前的users_weibo的ID类型Json串,解析为users_weibo表的主键相应的对象
     * 
     * @param jsonString
     * @return
     */
    private TimeMsgWeiboIdJsonBean getWeiboIdJsonBean(String jsonString) {
        try {
            if (!StringUtil.isNull(jsonString)) {
                return (TimeMsgWeiboIdJsonBean) JSONObject.toBean(JSONObject.fromObject(jsonString),
                                                                  TimeMsgWeiboIdJsonBean.class);
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("将Json串转换为待发送的微博对象时发生异常，微博内容：" + jsonString + ". \n异常原因：" + e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 检测当前传入的用户微博表的主键Id,检测其是否曾经发送失败过
     * 
     * @param jsonBean
     * @param uwid 用户微博表的主键Id
     * @return 如果是发送失败的类型返回true，不是则返回false
     */
    private boolean checkIsErrSendType(TimeMsgWeiboIdJsonBean jsonBean, long uwid) {
        for (TimeMsgWeiboId timeMsgWeiboId : jsonBean.getTimeMsgWeiboId()) {
            if (timeMsgWeiboId.getUwid() == uwid) {
                if (timeMsgWeiboId.isResult()) {// 处理成功了的
                    return false;
                }
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public void setUserIdFirstNumber(int userIdFirstNumber) {
        this.userIdFirstNumber = userIdFirstNumber;
    }

    public void setUsersTimeMsgDAO(UsersTimeMsgDAO usersTimeMsgDAO) {
        this.usersTimeMsgDAO = usersTimeMsgDAO;
    }

    public void setUsersTimeMsgBO(UsersTimeMsgBO usersTimeMsgBO) {
        this.usersTimeMsgBO = usersTimeMsgBO;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setWeiboSender(Map<String, WeiboSender> weiboSender) {
        this.weiboSender = weiboSender;
    }

    public void setUsersWeiboDAO(UsersWeiboDAO usersWeiboDAO) {
        this.usersWeiboDAO = usersWeiboDAO;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public static void main(String[] args) {
        String text = "{\"timeMsgWeiboId\":[{\"type\":\"Q\",\"uwid\":1},{\"type\":\"S\",\"uwid\":2}]}";
        TimeMsgWeiboIdJsonBean jsonBean = (TimeMsgWeiboIdJsonBean) JSONObject.toBean(JSONObject.fromObject(text),
                                                                                     TimeMsgWeiboIdJsonBean.class);
        System.out.println(jsonBean.getTimeMsgWeiboId().length);
        for (TimeMsgWeiboId weiboId : jsonBean.getTimeMsgWeiboId()) {
            System.out.println(weiboId.getType());
        }
    }

}
