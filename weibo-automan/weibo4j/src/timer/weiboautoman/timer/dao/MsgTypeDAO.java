package weiboautoman.timer.dao;

import weiboautoman.timer.dataobject.MsgType;

public interface MsgTypeDAO {
    int deleteByPrimaryKey(Integer id);

    void insert(MsgType record);

    void insertSelective(MsgType record);

    MsgType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MsgType record);

    int updateByPrimaryKey(MsgType record);
}