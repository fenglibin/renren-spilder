package net.fanli7.dao.ibatis;

import java.util.List;

import net.fanli7.dao.FanliTypeDAO;
import net.fanli7.dataobject.FanliType;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class IbatisFanliTypeDAO extends SqlMapClientDaoSupport implements FanliTypeDAO {

    public IbatisFanliTypeDAO(){
        super();
    }

    public int deleteByPrimaryKey(Integer id) {
        FanliType key = new FanliType();
        key.setId(id);
        int rows = getSqlMapClientTemplate().delete("fanli_type.deleteByPrimaryKey", key);
        return rows;
    }

    public void insert(FanliType record) {
        getSqlMapClientTemplate().insert("fanli_type.insert", record);
    }

    public void insertSelective(FanliType record) {
        getSqlMapClientTemplate().insert("fanli_type.insertSelective", record);
    }

    public FanliType selectByPrimaryKey(Integer id) {
        FanliType key = new FanliType();
        key.setId(id);
        FanliType record = (FanliType) getSqlMapClientTemplate().queryForObject("fanli_type.selectByPrimaryKey", key);
        return record;
    }

    public List<FanliType> selectMalls() {
        return (List<FanliType>) getSqlMapClientTemplate().queryForList("fanli_type.selectMalls");
    }

    public int updateByPrimaryKeySelective(FanliType record) {
        int rows = getSqlMapClientTemplate().update("fanli_type.updateByPrimaryKeySelective", record);
        return rows;
    }

    public int updateByPrimaryKey(FanliType record) {
        int rows = getSqlMapClientTemplate().update("fanli_type.updateByPrimaryKey", record);
        return rows;
    }
}
