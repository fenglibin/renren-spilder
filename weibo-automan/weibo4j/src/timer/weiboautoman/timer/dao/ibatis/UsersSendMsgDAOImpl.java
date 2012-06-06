package weiboautoman.timer.dao.ibatis;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import weiboautoman.timer.dao.UsersSendMsgDAO;
import weiboautoman.timer.dataobject.UsersSendMsg;

public class UsersSendMsgDAOImpl extends SqlMapClientDaoSupport implements UsersSendMsgDAO {

    public UsersSendMsgDAOImpl(){
        super();
    }

    public int deleteByPrimaryKey(Long id) {
        UsersSendMsg key = new UsersSendMsg();
        key.setId(id);
        int rows = getSqlMapClientTemplate().delete("users_send_msg.deleteByPrimaryKey", key);
        return rows;
    }

    public void insert(UsersSendMsg record) {
        getSqlMapClientTemplate().insert("users_send_msg.insert", record);
    }

    public void insertSelective(UsersSendMsg record) {
        getSqlMapClientTemplate().insert("users_send_msg.insertSelective", record);
    }

    public UsersSendMsg selectByPrimaryKey(Long id) {
        UsersSendMsg key = new UsersSendMsg();
        key.setId(id);
        UsersSendMsg record = (UsersSendMsg) getSqlMapClientTemplate().queryForObject("users_send_msg.selectByPrimaryKey",
                                                                                      key);
        return record;
    }

    public int updateByPrimaryKeySelective(UsersSendMsg record) {
        int rows = getSqlMapClientTemplate().update("users_send_msg.updateByPrimaryKeySelective", record);
        return rows;
    }

    public int updateByPrimaryKey(UsersSendMsg record) {
        int rows = getSqlMapClientTemplate().update("users_send_msg.updateByPrimaryKey", record);
        return rows;
    }
}
