package it.renren.spilder.xiaoshuo.dao.ibatis;

import it.renren.spilder.xiaoshuo.dao.ChaptersDAO;
import it.renren.spilder.xiaoshuo.dataobject.Chapters;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class ChaptersDAOImpl extends SqlMapClientDaoSupport implements ChaptersDAO {

    public ChaptersDAOImpl() {
        super();
    }

    public int deleteByPrimaryKey(Integer id) {
        Chapters key = new Chapters();
        key.setId(id);
        int rows = getSqlMapClientTemplate().delete("chapters.deleteByPrimaryKey", key);
        return rows;
    }

    public void insert(Chapters record) {
        getSqlMapClientTemplate().insert("chapters.insert", record);
    }

    public void insertSelective(Chapters record) {
        getSqlMapClientTemplate().insert("chapters.insertSelective", record);
    }

    public Chapters selectByPrimaryKey(Integer id) {
        Chapters key = new Chapters();
        key.setId(id);
        Chapters record = (Chapters) getSqlMapClientTemplate().queryForObject("chapters.selectByPrimaryKey", key);
        return record;
    }

    public int updateByPrimaryKeySelective(Chapters record) {
        int rows = getSqlMapClientTemplate().update("chapters.updateByPrimaryKeySelective", record);
        return rows;
    }

    public int updateByPrimaryKeyWithBLOBs(Chapters record) {
        int rows = getSqlMapClientTemplate().update("chapters.updateByPrimaryKeyWithBLOBs", record);
        return rows;
    }

    public int updateByPrimaryKeyWithoutBLOBs(Chapters record) {
        int rows = getSqlMapClientTemplate().update("chapters.updateByPrimaryKey", record);
        return rows;
    }
}