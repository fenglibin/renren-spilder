package weiboautoman.timer.dao;

import java.util.List;

import weiboautoman.timer.dataobject.UsersTimeMsg;

public interface UsersTimeMsgDAO {

    int deleteByPrimaryKey(Long id);

    void insert(UsersTimeMsg record);

    void insertSelective(UsersTimeMsg record);

    UsersTimeMsg selectByPrimaryKey(Long id);

    /**
     * 获取当前需要发送的用户数
     * 
     * @param userIdFirstNumber 用户ID的第一个数字
     * @param sendType 微博发送类型 Y表示立即发送，N表示定时发送
     * @return
     */
    long selectByUserIdFirstNumberLikeCount(String userIdFirstNumber, String sendType);

    /**
     * 查询的时候，根据用户ID的第一个数字进行like，只查询以这个数字开始的所有微博，减少查询的内容，分批进行查询。<br>
     * 分别传入的参数1-9中一个，并得到不同前缀开始的所有定时发送的微博内容
     * 
     * @param userIdFirstNumber 用户ID的第一个数字
     * @param sendType 微博发送类型 Y表示立即发送，N表示定时发送
     * @param start 分页查询开始的记录数
     * @param pageSize 需要查询的总记录数
     * @return
     */
    List<UsersTimeMsg> selectByUserIdFirstNumberLike(String userIdFirstNumber, String sendType, long start, int pageSize);

    int updateByPrimaryKeySelective(UsersTimeMsg record);

    int updateByPrimaryKey(UsersTimeMsg record);
}
