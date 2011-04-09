package it.renren.spilder.dao.ibatis;

import it.renren.spilder.dao.ArchivesDAO;
import it.renren.spilder.dataobject.ArchivesDO;
import it.renren.spilder.util.StringUtil;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class IbatisArchivesDAO extends SqlMapClientDaoSupport implements ArchivesDAO {

    private static final String Insert_Archives = "Insert_Archives";
    // ������׺������֧�ֶ����ͬ�ı�Ĳ�ѯ
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
}
