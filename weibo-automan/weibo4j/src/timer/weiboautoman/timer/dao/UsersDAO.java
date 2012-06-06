package weiboautoman.timer.dao;

import weiboautoman.timer.dataobject.Users;

public interface UsersDAO {
    int deleteByPrimaryKey(Long id);

    void insert(Users record);

    void insertSelective(Users record);

    Users selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);
}