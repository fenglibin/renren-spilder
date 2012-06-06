package weiboautoman.timer.dao;

import weiboautoman.timer.dataobject.UsersSendMsg;

public interface UsersSendMsgDAO {
    int deleteByPrimaryKey(Long id);

    void insert(UsersSendMsg record);

    void insertSelective(UsersSendMsg record);

    UsersSendMsg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UsersSendMsg record);

    int updateByPrimaryKey(UsersSendMsg record);
}