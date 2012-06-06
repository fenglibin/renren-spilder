package weiboautoman.timer.dao.ibatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import weiboautoman.timer.dao.UsersTimeMsgDAO;
import weiboautoman.timer.dataobject.UsersTimeMsg;
import weiboautoman.timer.util.StringUtil;

public class UsersTimeMsgDAOImpl extends SqlMapClientDaoSupport implements UsersTimeMsgDAO {

    public UsersTimeMsgDAOImpl(){
        super();
    }

    public int deleteByPrimaryKey(Long id) {
        UsersTimeMsg key = new UsersTimeMsg();
        key.setId(id);
        int rows = getSqlMapClientTemplate().delete("users_time_msg.deleteByPrimaryKey", key);
        return rows;
    }

    public void insert(UsersTimeMsg record) {
        getSqlMapClientTemplate().insert("users_time_msg.insert", record);
        throw new RuntimeException("test exception.");
    }

    public void insertSelective(UsersTimeMsg record) {
        getSqlMapClientTemplate().insert("users_time_msg.insertSelective", record);
    }

    public UsersTimeMsg selectByPrimaryKey(Long id) {
        UsersTimeMsg key = new UsersTimeMsg();
        key.setId(id);
        UsersTimeMsg record = (UsersTimeMsg) getSqlMapClientTemplate().queryForObject("users_time_msg.selectByPrimaryKey",
                                                                                      key);
        return record;
    }

    @Override
    public long selectByUserIdFirstNumberLikeCount(String userIdFirstNumber, String sendType) {
        if (StringUtil.isNull(userIdFirstNumber)) {
            userIdFirstNumber = "0";
        }
        Map param = new HashMap();
        int intUserIdFirstNumber = Integer.parseInt(userIdFirstNumber);
        if (intUserIdFirstNumber > 0) {
            userIdFirstNumber += "%";
            param.put("userIdFirstNumber", userIdFirstNumber);
        }
        param.put("sendType", sendType);
        return (Long) getSqlMapClientTemplate().queryForObject("users_time_msg.selectByUserIdFirstNumberLikeCount",
                                                               param);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UsersTimeMsg> selectByUserIdFirstNumberLike(String userIdFirstNumber, String sendType, long start,
                                                            int pageSize) {
        if (StringUtil.isNull(userIdFirstNumber)) {
            userIdFirstNumber = "0";
        }
        Map param = new HashMap();
        int intUserIdFirstNumber = Integer.parseInt(userIdFirstNumber);
        if (intUserIdFirstNumber > 0) {
            userIdFirstNumber += "%";
            param.put("userIdFirstNumber", userIdFirstNumber);
        }
        param.put("sendType", sendType);
        param.put("start", start);
        param.put("pageSize", pageSize);
        return getSqlMapClientTemplate().queryForList("users_time_msg.selectByUserIdFirstNumberLike", param);
    }

    public int updateByPrimaryKeySelective(UsersTimeMsg record) {
        int rows = getSqlMapClientTemplate().update("users_time_msg.updateByPrimaryKeySelective", record);
        return rows;
    }

    public int updateByPrimaryKey(UsersTimeMsg record) {
        int rows = getSqlMapClientTemplate().update("users_time_msg.updateByPrimaryKey", record);
        return rows;
    }

}
