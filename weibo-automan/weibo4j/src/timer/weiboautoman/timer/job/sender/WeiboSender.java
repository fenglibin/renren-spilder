package weiboautoman.timer.job.sender;

import weiboautoman.timer.core.SendResult;
import weiboautoman.timer.core.SendStatusEnum;
import weiboautoman.timer.dao.UsersTimeMsgDAO;
import weiboautoman.timer.dataobject.UsersTimeMsg;
import weiboautoman.timer.dataobject.vo.UsersTimeMsgVO;
import weiboautoman.timer.util.StringUtil;

/**
 * 类WeiboSender.java的实现描述：发送微博的抽象类
 * 
 * @author fenglibin 2011-10-18 上午08:49:57
 */
public abstract class WeiboSender {

    private UsersTimeMsgDAO usersTimeMsgDAO;
    /**
     * 微博配图的前缀物理地址
     */
    private String          imagePath;

    /**
     * 微博发送方法
     * 
     * @param msgVO
     * @return 发送结果 成功返回true，失败返回false；另外，如果传入的类型不属于当前微博类型，也返回成功，因为没有发送。
     */
    public abstract SendResult send(UsersTimeMsgVO msgVO);

    /**
     * 获取微博的类型，如果没有类型，则返回空字符串
     * 
     * @param msgVO
     * @return 微博的类型
     */
    protected String getWeiboType(UsersTimeMsgVO msgVO) {
        String type = "";
        type = msgVO == null ? "" : msgVO.getWeiboType();
        if (StringUtil.isNull(type)) {
            type = "";
        }
        return type;
    }

    protected boolean isSendCheck(UsersTimeMsgVO msgVO) {
        boolean isSend = Boolean.FALSE;
        UsersTimeMsg usersTimeMsg = usersTimeMsgDAO.selectByPrimaryKey(msgVO.getId());
        if (SendStatusEnum.SEND_SUCCESS.getValue().equalsIgnoreCase(usersTimeMsg.getIsSend())) {
            isSend = Boolean.TRUE;
        }
        return isSend;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setUsersTimeMsgDAO(UsersTimeMsgDAO usersTimeMsgDAO) {
        this.usersTimeMsgDAO = usersTimeMsgDAO;
    }

}
