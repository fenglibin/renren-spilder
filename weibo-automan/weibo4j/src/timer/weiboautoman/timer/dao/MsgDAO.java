package weiboautoman.timer.dao;

import weiboautoman.timer.dataobject.Msg;

public interface MsgDAO {

    int deleteByPrimaryKey(Long id);

    void insert(Msg record);

    void insertSelective(Msg record);

    Msg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Msg record);

    int updateByPrimaryKey(Msg record);

    /**
     * 根据用户Id随机查询一条这个用户未发送过的记录，如果用户指定了内容的分类，则从指定分类中随机查询一条未使用过的记录
     * 
     * @param userId 用户的ID
     * @param typeId 内容库分类的Id
     * @return
     */
    Msg selectOneNotSendMessage(Long userId, int typeId);
}
