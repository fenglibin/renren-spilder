package net.fanli7.dao.ibatis;

import java.util.List;

import net.fanli7.dao.FanliMallDAO;
import net.fanli7.dataobject.FanliMall;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class IbatisFanliMallDAO extends SqlMapClientDaoSupport implements FanliMallDAO {

    public IbatisFanliMallDAO(){
        super();
    }

    public int deleteByPrimaryKey(Integer id) {
        FanliMall key = new FanliMall();
        key.setId(id);
        int rows = getSqlMapClientTemplate().delete("fanli_mall.deleteByPrimaryKey", key);
        return rows;
    }

    public void insert(FanliMall record) {
        getSqlMapClientTemplate().insert("fanli_mall.insert", record);
    }

    public void insertSelective(FanliMall record) {
        getSqlMapClientTemplate().insert("fanli_mall.insertSelective", record);
    }

    public FanliMall selectByPrimaryKey(Integer id) {
        FanliMall key = new FanliMall();
        key.setId(id);
        FanliMall record = (FanliMall) getSqlMapClientTemplate().queryForObject("fanli_mall.selectByPrimaryKey", key);
        return record;
    }

    public FanliMall selectByTitle(String title) {
        FanliMall key = new FanliMall();
        key.setTitle(title);
        FanliMall record = (FanliMall) getSqlMapClientTemplate().queryForObject("fanli_mall.selectByTitle", key);
        return record;
    }

    public List<FanliMall> selectAll() {
        List<FanliMall> records = (List<FanliMall>) getSqlMapClientTemplate().queryForList("fanli_mall.selectAll");
        return records;
    }

    public int updateByPrimaryKeySelective(FanliMall record) {
        int rows = getSqlMapClientTemplate().update("fanli_mall.updateByPrimaryKeySelective", record);
        return rows;
    }

    public int updateByPrimaryKeyWithBLOBs(FanliMall record) {
        int rows = getSqlMapClientTemplate().update("fanli_mall.updateByPrimaryKeyWithBLOBs", record);
        return rows;
    }

    public int updateByPrimaryKeyWithoutBLOBs(FanliMall record) {
        int rows = getSqlMapClientTemplate().update("fanli_mall.updateByPrimaryKey", record);
        return rows;
    }
}
