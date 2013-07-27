package it.renren.spilder.xiaoshuo.dao;

import it.renren.spilder.xiaoshuo.dataobject.Chapters;

public interface ChaptersDAO {
    int deleteByPrimaryKey(Integer id);

    void insert(Chapters record);

    void insertSelective(Chapters record);

    Chapters selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Chapters record);

    int updateByPrimaryKeyWithBLOBs(Chapters record);

    int updateByPrimaryKeyWithoutBLOBs(Chapters record);
}