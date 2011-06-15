package it.renren.spilder.dao.ibatis;

import java.util.List;

import it.renren.spilder.dao.ArchivesDAO;
import it.renren.spilder.dataobject.ArchivesDO;
import it.renren.spilder.util.StringUtil;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class IbatisArchivesDAO extends SqlMapClientDaoSupport implements ArchivesDAO {

    private static final String Insert_Archives                 = "Insert_Archives";
    private static final String SELECT_NULL_DESCRIPTION_RECORDS = "SELECT_NULL_DESCRIPTION_RECORDS";
    private static final String UPDATE_DESCRIPTION              = "UPDATE_DESCRIPTION";
    // 条件后缀，用于支持多个不同的表的查询
    private String              conditionSuffix;

    @Override
    public void insertArchives(ArchivesDO archivesDO) {
        String statementName = Insert_Archives;
        if (!StringUtil.isNull(conditionSuffix)) {
            statementName = statementName + conditionSuffix;
        }
        getSqlMapClientTemplate().insert(statementName, archivesDO);
    }

    public void setConditionSuffix(String conditionSuffix) {
        this.conditionSuffix = conditionSuffix;
    }

    @Override
    public List<ArchivesDO> selectNullDescriptionRecords() {
        String statementName = SELECT_NULL_DESCRIPTION_RECORDS;
        if (!StringUtil.isNull(conditionSuffix)) {
            statementName = statementName + conditionSuffix;
        }
        return getSqlMapClientTemplate().queryForList(statementName);
    }

    @Override
    public int updateDescription(ArchivesDO archivesDO) {
        String statementName = UPDATE_DESCRIPTION;
        if (!StringUtil.isNull(conditionSuffix)) {
            statementName = statementName + conditionSuffix;
        }
        return getSqlMapClientTemplate().update(statementName, archivesDO);
    }
}
