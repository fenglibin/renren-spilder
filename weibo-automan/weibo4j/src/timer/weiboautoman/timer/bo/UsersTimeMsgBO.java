package weiboautoman.timer.bo;

import weiboautoman.timer.core.SendStatusEnum;
import weiboautoman.timer.dao.UsersTimeMsgDAO;
import weiboautoman.timer.dataobject.UsersTimeMsg;
import weiboautoman.timer.util.StringUtil;

public class UsersTimeMsgBO {

    private UsersTimeMsgDAO usersTimeMsgDAO;

    /**
     * 更新一条已经发送的微博记录的状态为已发送
     * 
     * @param id
     */
    public void updateUserTimeMsgSendStatusAndResult(long id, String sendResult) {
        UsersTimeMsg msg = new UsersTimeMsg();
        msg.setId(id);
        if (!StringUtil.isNull(sendResult)) {/* 有发送失败的 */
            msg.setIsSend(SendStatusEnum.SEND_ERROR.getValue());
            msg.setSendResult(sendResult);
        } else {
            msg.setIsSend(SendStatusEnum.SEND_SUCCESS.getValue());
            msg.setSendResult("");
        }
        usersTimeMsgDAO.updateByPrimaryKeySelective(msg);
    }

    public void setUsersTimeMsgDAO(UsersTimeMsgDAO usersTimeMsgDAO) {
        this.usersTimeMsgDAO = usersTimeMsgDAO;
    }
}
