package it.renren.spilder.xiaoshuo.dao.ibatis;

import it.renren.spilder.util.StringUtil;
import it.renren.spilder.xiaoshuo.dao.BooksDAO;
import it.renren.spilder.xiaoshuo.dataobject.Books;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class BooksDAOImpl extends SqlMapClientDaoSupport implements BooksDAO {

    public BooksDAOImpl(){
        super();
    }

    public int deleteByPrimaryKey(Integer id) {
        Books key = new Books();
        key.setId(id);
        int rows = getSqlMapClientTemplate().delete("books.deleteByPrimaryKey", key);
        return rows;
    }

    public void insert(Books record) {
        getSqlMapClientTemplate().insert("books.insert", record);
    }

    public void insertSelective(Books record) {
        getSqlMapClientTemplate().insert("books.insertSelective", record);
    }

    public Books selectByPrimaryKey(Integer id) {
        Books key = new Books();
        key.setId(id);
        Books record = (Books) getSqlMapClientTemplate().queryForObject("books.selectByPrimaryKey", key);
        return record;
    }

    public Books selectBySpilderUrl(String url) {
        if (StringUtil.isEmpty(url)) {
            return null;
        }
        Books key = new Books();
        key.setSpilderurl(url);
        Books record = (Books) getSqlMapClientTemplate().queryForObject("books.selectBySpilderUrl", key);
        return record;
    }

    @Override
    public List<String> selectSpilderUrls() {
        return getSqlMapClientTemplate().queryForList("books.selectSpilderUrls");
    }

    public int updateByPrimaryKeySelective(Books record) {
        int rows = getSqlMapClientTemplate().update("books.updateByPrimaryKeySelective", record);
        return rows;
    }

    public int updateByPrimaryKey(Books record) {
        int rows = getSqlMapClientTemplate().update("books.updateByPrimaryKey", record);
        return rows;
    }

}
