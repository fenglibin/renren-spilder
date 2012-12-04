package net.fanli7.dao;

import java.util.List;

import net.fanli7.dataobject.FanliType;

public interface FanliTypeDAO {

    int deleteByPrimaryKey(Integer id);

    void insert(FanliType record);

    void insertSelective(FanliType record);

    FanliType selectByPrimaryKey(Integer id);

    List<FanliType> selectMalls();

    int updateByPrimaryKeySelective(FanliType record);

    int updateByPrimaryKey(FanliType record);
}
