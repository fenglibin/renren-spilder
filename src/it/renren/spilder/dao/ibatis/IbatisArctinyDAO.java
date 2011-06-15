package it.renren.spilder.dao.ibatis;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import it.renren.spilder.dao.ArctinyDAO;
import it.renren.spilder.dataobject.ArctinyDO;
import it.renren.spilder.util.StringUtil;

public class IbatisArctinyDAO extends SqlMapClientDaoSupport implements ArctinyDAO {

    private static final String Insert_Arctiny           = "Insert_Arctiny";
    private static final String Select_Current_ArticleId = "Select_Current_ArticleId";
    private static final String Update_Current_ArticleId = "Update_Current_ArticleId";
    // 条件后缀，用于支持多个不同的表的查询
    private String              conditionSuffix;

    @Override
    public Object insertArctiny(ArctinyDO arctinyDO) {
        String statementName = Insert_Arctiny;
        if (!StringUtil.isNull(conditionSuffix)) {
            statementName = statementName + conditionSuffix;
        }
        return getSqlMapClientTemplate().insert(statementName, arctinyDO);
    }

    @Override
    public ArctinyDO selectArctinyByTypeId(ArctinyDO arctinyDO) {
        String statementName = Select_Current_ArticleId;
        if (!StringUtil.isNull(conditionSuffix)) {
            statementName = statementName + conditionSuffix;
        }
        return (ArctinyDO) getSqlMapClientTemplate().queryForObject(statementName, arctinyDO);
    }

    @Override
    public void updateArctinyTypeidById(ArctinyDO arctinyDO) {
        String statementName = Update_Current_ArticleId;
        if (!StringUtil.isNull(conditionSuffix)) {
            statementName = statementName + conditionSuffix;
        }
        getSqlMapClientTemplate().update(statementName, arctinyDO);
    }

    public void setConditionSuffix(String conditionSuffix) {
        this.conditionSuffix = conditionSuffix;
    }
}
