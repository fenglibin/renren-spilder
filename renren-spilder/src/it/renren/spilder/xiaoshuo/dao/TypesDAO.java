package it.renren.spilder.xiaoshuo.dao;

import it.renren.spilder.xiaoshuo.dataobject.Types;

import java.util.List;

public interface TypesDAO {

    int deleteByPrimaryKey(Integer id);

    void insert(Types record);

    void insertSelective(Types record);

    Types selectByPrimaryKey(Integer id);

    List<Types> selectAllTypes();

    int updateByPrimaryKeySelective(Types record);

    int updateByPrimaryKey(Types record);
}
