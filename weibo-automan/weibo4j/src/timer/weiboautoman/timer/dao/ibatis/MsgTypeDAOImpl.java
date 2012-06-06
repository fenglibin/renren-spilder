package weiboautoman.timer.dao.ibatis;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import weiboautoman.timer.dao.MsgTypeDAO;
import weiboautoman.timer.dataobject.MsgType;

public class MsgTypeDAOImpl extends SqlMapClientDaoSupport implements MsgTypeDAO {

    public MsgTypeDAOImpl() {
        super();
    }

    public int deleteByPrimaryKey(Integer id) {
        MsgType key = new MsgType();
        key.setId(id);
        int rows = getSqlMapClientTemplate().delete("msg_type.deleteByPrimaryKey", key);
        return rows;
    }

    public void insert(MsgType record) {
        getSqlMapClientTemplate().insert("msg_type.insert", record);
    }

    public void insertSelective(MsgType record) {
        getSqlMapClientTemplate().insert("msg_type.insertSelective", record);
    }

    public MsgType selectByPrimaryKey(Integer id) {
        MsgType key = new MsgType();
        key.setId(id);
        MsgType record = (MsgType) getSqlMapClientTemplate().queryForObject("msg_type.selectByPrimaryKey", key);
        return record;
    }

    public int updateByPrimaryKeySelective(MsgType record) {
        int rows = getSqlMapClientTemplate().update("msg_type.updateByPrimaryKeySelective", record);
        return rows;
    }

    public int updateByPrimaryKey(MsgType record) {
        int rows = getSqlMapClientTemplate().update("msg_type.updateByPrimaryKey", record);
        return rows;
    }
}