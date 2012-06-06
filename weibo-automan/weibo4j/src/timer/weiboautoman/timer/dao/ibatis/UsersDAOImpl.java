package weiboautoman.timer.dao.ibatis;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import weiboautoman.timer.dao.UsersDAO;
import weiboautoman.timer.dataobject.Users;

public class UsersDAOImpl extends SqlMapClientDaoSupport implements UsersDAO {

    public UsersDAOImpl() {
        super();
    }

    public int deleteByPrimaryKey(Long id) {
        Users key = new Users();
        key.setId(id);
        int rows = getSqlMapClientTemplate().delete("users.deleteByPrimaryKey", key);
        return rows;
    }

    public void insert(Users record) {
        getSqlMapClientTemplate().insert("users.insert", record);
    }

    public void insertSelective(Users record) {
        getSqlMapClientTemplate().insert("users.insertSelective", record);
    }

    public Users selectByPrimaryKey(Long id) {
        Users key = new Users();
        key.setId(id);
        Users record = (Users) getSqlMapClientTemplate().queryForObject("users.selectByPrimaryKey", key);
        return record;
    }

    public int updateByPrimaryKeySelective(Users record) {
        int rows = getSqlMapClientTemplate().update("users.updateByPrimaryKeySelective", record);
        return rows;
    }

    public int updateByPrimaryKey(Users record) {
        int rows = getSqlMapClientTemplate().update("users.updateByPrimaryKey", record);
        return rows;
    }
}