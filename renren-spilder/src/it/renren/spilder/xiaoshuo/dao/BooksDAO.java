package it.renren.spilder.xiaoshuo.dao;

import it.renren.spilder.xiaoshuo.dataobject.Books;

import java.util.List;

public interface BooksDAO {

    int deleteByPrimaryKey(Integer id);

    void insert(Books record);

    void insertSelective(Books record);

    Books selectByPrimaryKey(Integer id);

    Books selectBySpilderUrl(String url);

    List<String> selectSpilderUrls();

    int updateByPrimaryKeySelective(Books record);

    int updateByPrimaryKey(Books record);
}
