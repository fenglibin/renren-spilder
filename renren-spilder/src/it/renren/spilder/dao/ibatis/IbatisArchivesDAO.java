package it.renren.spilder.dao.ibatis;

import java.util.List;

import it.renren.spilder.dao.ArchivesDAO;
import it.renren.spilder.dataobject.ArchivesDO;
import it.renren.spilder.util.StringUtil;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class IbatisArchivesDAO extends SqlMapClientDaoSupport implements ArchivesDAO {

    private static final String Insert_Archives                   = "Insert_Archives";
    private static final String SELECT_NULL_DESCRIPTION_RECORDS   = "SELECT_NULL_DESCRIPTION_RECORDS";
    private static final String UPDATE_DESCRIPTION                = "UPDATE_DESCRIPTION";
    private static final String UPDATE_TITLE_KEYWORDS_DESCRIPTION = "UPDATE_TITLE_KEYWORDS_DESCRIPTION";
    // 条件后缀，用于支持多个不同的表的查询
    private String              tablePrefix;
    // dedecms的版本，默认是5.6
    private String              dedecmsVersion                    = "";

    @Override
    public void insertArchives(ArchivesDO archivesDO) {
        String this_insert_archives = Insert_Archives + dedecmsVersion;
        archivesDO.setTablePrefix(StringUtil.getTablePrefix(tablePrefix));
        getSqlMapClientTemplate().insert(this_insert_archives, archivesDO);
    }

    @Override
    public List<ArchivesDO> selectNullDescriptionRecords() {
        ArchivesDO archivesDO = new ArchivesDO();
        archivesDO.setTablePrefix(StringUtil.getTablePrefix(tablePrefix));
        return getSqlMapClientTemplate().queryForList(SELECT_NULL_DESCRIPTION_RECORDS, archivesDO);
    }

    @Override
    public int updateDescription(ArchivesDO archivesDO) {
        archivesDO.setTablePrefix(StringUtil.getTablePrefix(tablePrefix));
        return getSqlMapClientTemplate().update(UPDATE_DESCRIPTION, archivesDO);
    }

    @Override
    public int updateTitleKeywordsDescription(ArchivesDO archivesDO) {
        archivesDO.setTablePrefix(StringUtil.getTablePrefix(tablePrefix));
        return getSqlMapClientTemplate().update(UPDATE_TITLE_KEYWORDS_DESCRIPTION, archivesDO);
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public void setDedecmsVersion(String dedecmsVersion) {
        this.dedecmsVersion = dedecmsVersion;
    }

}
