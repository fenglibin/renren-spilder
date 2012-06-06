package weiboautoman.timer.dao.ibatis;

import java.util.HashMap;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import weiboautoman.timer.dao.MsgDAO;
import weiboautoman.timer.dataobject.Msg;

public class MsgDAOImpl extends SqlMapClientDaoSupport implements MsgDAO {

    public MsgDAOImpl(){
        super();
    }

    public int deleteByPrimaryKey(Long id) {
        Msg key = new Msg();
        key.setId(id);
        int rows = getSqlMapClientTemplate().delete("msg.deleteByPrimaryKey", key);
        return rows;
    }

    public void insert(Msg record) {
        getSqlMapClientTemplate().insert("msg.insert", record);
    }

    public void insertSelective(Msg record) {
        getSqlMapClientTemplate().insert("msg.insertSelective", record);
    }

    public Msg selectByPrimaryKey(Long id) {
        Msg key = new Msg();
        key.setId(id);
        Msg record = (Msg) getSqlMapClientTemplate().queryForObject("msg.selectByPrimaryKey", key);
        return record;
    }

    public int updateByPrimaryKeySelective(Msg record) {
        int rows = getSqlMapClientTemplate().update("msg.updateByPrimaryKeySelective", record);
        return rows;
    }

    public int updateByPrimaryKey(Msg record) {
        int rows = getSqlMapClientTemplate().update("msg.updateByPrimaryKey", record);
        return rows;
    }

    @Override
    public Msg selectOneNotSendMessage(Long userId, int typeId) {
        Map para = new HashMap();
        para.put("userId", userId);
        para.put("typeId", typeId);
        Msg record = (Msg) getSqlMapClientTemplate().queryForObject("msg.selectOneNotSendMessage", para);
        return record;
    }
}
