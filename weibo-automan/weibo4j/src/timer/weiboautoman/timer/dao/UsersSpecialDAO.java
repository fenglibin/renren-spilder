package weiboautoman.timer.dao;

import java.util.List;

import weiboautoman.timer.dataobject.UsersSpecial;

public interface UsersSpecialDAO {

    int deleteByPrimaryKey(Long userId);

    void insert(UsersSpecial record);

    void insertSelective(UsersSpecial record);

    UsersSpecial selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(UsersSpecial record);

    int updateByPrimaryKey(UsersSpecial record);

    /**
     * 查询总记录数
     * 
     * @return
     */
    long selectTotalCount();

    /**
     * 分页查询
     * 
     * @param start 开始的记录数
     * @param pageSize 每页查询的记录数
     * @return
     */
    List<UsersSpecial> selectOnePage(long start, int pageSize);
}
