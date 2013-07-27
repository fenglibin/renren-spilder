package it.renren.spilder.xiaoshuo.dao.ibatis;

import it.renren.spilder.xiaoshuo.dao.TypesDAO;
import it.renren.spilder.xiaoshuo.dataobject.Types;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class TypesDAOImpl extends SqlMapClientDaoSupport implements TypesDAO {

    public TypesDAOImpl(){
        super();
    }

    public int deleteByPrimaryKey(Integer id) {
        Types key = new Types();
        key.setId(id);
        int rows = getSqlMapClientTemplate().delete("types.deleteByPrimaryKey", key);
        return rows;
    }

    public void insert(Types record) {
        getSqlMapClientTemplate().insert("types.insert", record);
    }

    public void insertSelective(Types record) {
        getSqlMapClientTemplate().insert("types.insertSelective", record);
    }

    public Types selectByPrimaryKey(Integer id) {
        Types key = new Types();
        key.setId(id);
        Types record = (Types) getSqlMapClientTemplate().queryForObject("types.selectByPrimaryKey", key);
        return record;
    }

    @Override
    public List<Types> selectAllTypes() {
        // TODO Auto-generated method stub
        return getSqlMapClientTemplate().queryForList("types.selectAllTypes");
    }

    public int updateByPrimaryKeySelective(Types record) {
        int rows = getSqlMapClientTemplate().update("types.updateByPrimaryKeySelective", record);
        return rows;
    }

    public int updateByPrimaryKey(Types record) {
        int rows = getSqlMapClientTemplate().update("types.updateByPrimaryKey", record);
        return rows;
    }

}
