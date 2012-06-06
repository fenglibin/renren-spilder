package weiboautoman.timer.dao;

import java.util.List;

import weiboautoman.timer.dataobject.UsersWeibo;
import weiboautoman.timer.dataobject.vo.UsersWeiboVO;

public interface UsersWeiboDAO {

    int deleteByPrimaryKey(Long id);

    void insert(UsersWeibo record);

    void insertSelective(UsersWeibo record);

    UsersWeibo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UsersWeibo record);

    int updateByPrimaryKey(UsersWeibo record);

    UsersWeiboVO selectByPrimaryKeySmall(Long id);

    /**
     * 根据用户的ID，获取这个用户所有配置的微博列表
     * 
     * @param userId
     * @return
     */
    List<UsersWeiboVO> selectUserWeiboByUserId(Long userId);
}
