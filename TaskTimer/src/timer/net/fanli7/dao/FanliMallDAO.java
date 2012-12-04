package net.fanli7.dao;

import java.util.List;

import net.fanli7.dataobject.FanliMall;

public interface FanliMallDAO {

    int deleteByPrimaryKey(Integer id);

    void insert(FanliMall record);

    void insertSelective(FanliMall record);

    FanliMall selectByPrimaryKey(Integer id);

    FanliMall selectByTitle(String title);

    List<FanliMall> selectAll();

    int updateByPrimaryKeySelective(FanliMall record);

    int updateByPrimaryKeyWithBLOBs(FanliMall record);

    int updateByPrimaryKeyWithoutBLOBs(FanliMall record);
}
