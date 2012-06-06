package weiboautoman.timer.job;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weiboautoman.timer.core.Constants;
import weiboautoman.timer.core.SendStatusEnum;
import weiboautoman.timer.dao.MsgDAO;
import weiboautoman.timer.dao.UsersDAO;
import weiboautoman.timer.dao.UsersSendMsgDAO;
import weiboautoman.timer.dao.UsersSpecialDAO;
import weiboautoman.timer.dao.UsersTimeMsgDAO;
import weiboautoman.timer.dao.UsersWeiboDAO;
import weiboautoman.timer.dataobject.Msg;
import weiboautoman.timer.dataobject.Users;
import weiboautoman.timer.dataobject.UsersSendMsg;
import weiboautoman.timer.dataobject.UsersSpecial;
import weiboautoman.timer.dataobject.UsersTimeMsg;
import weiboautoman.timer.dataobject.vo.TimeMsgWeiboId;
import weiboautoman.timer.dataobject.vo.TimeMsgWeiboIdJsonBean;
import weiboautoman.timer.dataobject.vo.UsersWeiboVO;
import weiboautoman.timer.util.DateUtil;
import weiboautoman.timer.util.NumberUtil;

/**
 * 类SendMessage2SpecailUsers.java的实现描述：给一部份特殊用户自动发送微博
 * 
 * @author Administrator 2011-11-26 下午07:40:18
 */
public class SendMessage2SpecailUsers extends Thread {

    private static Logger    log                 = LoggerFactory.getLogger(WeiboSenderThread.class);
    private UsersSpecialDAO  usersSpecialDAO;
    private UsersSendMsgDAO  usersSendMsgDAO;
    private UsersWeiboDAO    usersWeiboDAO;
    private UsersTimeMsgDAO  usersTimeMsgDAO;
    private MsgDAO           msgDAO;
    private UsersDAO         usersDAO;
    /* 没有设置发送间隔时间的，则默认发送间隔时间为30分钟 */
    private static final int defaultTimeInterval = 30;

    public void run() {
        long totalCount = usersSpecialDAO.selectTotalCount();
        if (totalCount > 0) {
            long pages = NumberUtil.getPages(totalCount, Constants.DEFAULT_PAGE_SIZE);
            if (log.isDebugEnabled()) {
                log.debug("totalCount:" + totalCount + ",pages:" + pages);
            }
            for (long currentPage = 1; currentPage <= pages; currentPage++) {/* 分页处理 */
                long start = (currentPage - 1) * Constants.DEFAULT_PAGE_SIZE;
                List<UsersSpecial> usersSpecialList = usersSpecialDAO.selectOnePage(start, Constants.DEFAULT_PAGE_SIZE);
                for (UsersSpecial user : usersSpecialList) {/* 对每个用户进行分别处理 */
                    try {
                        int timeInterval = user.getTimeInterval();
                        if (timeInterval <= 0) {
                            timeInterval = defaultTimeInterval;
                        }
                        /* 根据用户ID获取一个用户 */
                        Users oneUser = usersDAO.selectByPrimaryKey(user.getUserId());
                        /* 最后一次执行定时发送微博的时间 */
                        Date lastTaskDate = oneUser.getLastTaskDate();
                        Date now = new Date();
                        /* 如果没有执行定时微博，则直接执行发送 */
                        if (lastTaskDate != null) {
                            /* 判断上次最后一次的发送时间加上间隔的分钟数，是否小于等于当前系统时间，如果小于等于当前系统时间，那说明应该给这个用户发送微博了，否则不发送 */
                            if (DateUtil.date2TimeStamp(DateUtil.addMinutes(lastTaskDate, timeInterval)) > DateUtil.date2TimeStamp(now)) {
                                continue;
                            }
                        }
                        /* 将当前系统时间做为最后一次发送时间 */
                        oneUser.setLastTaskDate(now);
                        usersDAO.updateByPrimaryKeySelective(oneUser);

                        Msg msg = msgDAO.selectOneNotSendMessage(user.getUserId(), user.getTypeId());
                        if (msg == null) {
                            if (log.isWarnEnabled()) {
                                log.warn("根据信息查询不到未发送的记录,user_id:" + user.getUserId() + ",type_id:" + user.getTypeId());
                            }
                            continue;
                        }
                        List<UsersWeiboVO> getUserWeiboList = usersWeiboDAO.selectUserWeiboByUserId(user.getUserId());
                        if (getUserWeiboList == null || getUserWeiboList.size() == 0) {
                            if (log.isWarnEnabled()) {
                                log.warn("根据用户ID获取不到该用户的微博列表,user_id:" + user.getUserId());
                            }
                            continue;
                        }
                        /* 将发送记录保存到发送记录表中 */
                        UsersSendMsg usersSendMsg = new UsersSendMsg();
                        usersSendMsg.setMsgId(msg.getId());
                        usersSendMsg.setUserId(user.getUserId());
                        usersSendMsg.setSendDate(new Date());
                        usersSendMsgDAO.insert(usersSendMsg);

                        TimeMsgWeiboIdJsonBean sendWeiboIdJsonBean = new TimeMsgWeiboIdJsonBean();
                        TimeMsgWeiboId[] timeMsgWeiboIdArray = new TimeMsgWeiboId[getUserWeiboList.size()];
                        int index = 0;
                        for (UsersWeiboVO usersWeiboVO : getUserWeiboList) {/* 针对这个用户的不同微博进行发送 */
                            TimeMsgWeiboId timeMsgWeiboId = new TimeMsgWeiboId();
                            timeMsgWeiboId.setUwid(usersWeiboVO.getId());
                            timeMsgWeiboId.setType(usersWeiboVO.getWeiboType());
                            timeMsgWeiboIdArray[index] = timeMsgWeiboId;
                            index++;
                        }
                        sendWeiboIdJsonBean.setTimeMsgWeiboId(timeMsgWeiboIdArray);
                        UsersTimeMsg userTimeMsg = new UsersTimeMsg();
                        userTimeMsg.setIsSend(SendStatusEnum.NOT_SEND.getValue());
                        userTimeMsg.setMsgContent(msg.getContent());
                        userTimeMsg.setMsgId(msg.getId());
                        userTimeMsg.setMsgPicture(msg.getPicture());
                        userTimeMsg.setSendTime(DateUtil.date2TimeStamp(now).intValue());
                        userTimeMsg.setSendType("Y");
                        userTimeMsg.setUserId(user.getUserId());
                        userTimeMsg.setWeiboId(JSONObject.fromObject(sendWeiboIdJsonBean).toString());
                        /* 将数据插入到定时发送表中 */
                        usersTimeMsgDAO.insert(userTimeMsg);
                    } catch (Exception e) {
                        if (log.isErrorEnabled()) {
                            log.error("给用户ID为：" + user.getUserId() + " 生成特殊用户发送消息时发生异常：" + e.getMessage(), e);
                        }
                    }
                }
            }
        }
    }

    public void setUsersSpecialDAO(UsersSpecialDAO usersSpecialDAO) {
        this.usersSpecialDAO = usersSpecialDAO;
    }

    public void setUsersSendMsgDAO(UsersSendMsgDAO usersSendMsgDAO) {
        this.usersSendMsgDAO = usersSendMsgDAO;
    }

    public void setUsersWeiboDAO(UsersWeiboDAO usersWeiboDAO) {
        this.usersWeiboDAO = usersWeiboDAO;
    }

    public void setUsersTimeMsgDAO(UsersTimeMsgDAO usersTimeMsgDAO) {
        this.usersTimeMsgDAO = usersTimeMsgDAO;
    }

    public void setMsgDAO(MsgDAO msgDAO) {
        this.msgDAO = msgDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

}
