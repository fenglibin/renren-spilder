package weiboautoman.timer.dao.ibatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import weiboautoman.timer.dao.UsersSpecialDAO;
import weiboautoman.timer.dataobject.UsersSpecial;

public class UsersSpecialDAOImpl extends SqlMapClientDaoSupport implements UsersSpecialDAO {

    public UsersSpecialDAOImpl(){
        super();
    }

    public int deleteByPrimaryKey(Long userId) {
        UsersSpecial key = new UsersSpecial();
        key.setUserId(userId);
        int rows = getSqlMapClientTemplate().delete("users_special.deleteByPrimaryKey", key);
        return rows;
    }

    public void insert(UsersSpecial record) {
        getSqlMapClientTemplate().insert("users_special.insert", record);
    }

    public void insertSelective(UsersSpecial record) {
        getSqlMapClientTemplate().insert("users_special.insertSelective", record);
    }

    public UsersSpecial selectByPrimaryKey(Long userId) {
        UsersSpecial key = new UsersSpecial();
        key.setUserId(userId);
        UsersSpecial record = (UsersSpecial) getSqlMapClientTemplate().queryForObject("users_special.selectByPrimaryKey",
                                                                                      key);
        return record;
    }

    public int updateByPrimaryKeySelective(UsersSpecial record) {
        int rows = getSqlMapClientTemplate().update("users_special.updateByPrimaryKeySelective", record);
        return rows;
    }

    public int updateByPrimaryKey(UsersSpecial record) {
        int rows = getSqlMapClientTemplate().update("users_special.updateByPrimaryKey", record);
        return rows;
    }

    @Override
    public long selectTotalCount() {
        return (Long) getSqlMapClientTemplate().queryForObject("users_special.selectTotalCount");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UsersSpecial> selectOnePage(long start, int pageSize) {
        Map param = new HashMap();
        param.put("start", start);
        param.put("pageSize", pageSize);
        return getSqlMapClientTemplate().queryForList("users_special.selectOnePage", param);
    }
}
