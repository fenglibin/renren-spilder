package it.renren.spilder.dao.ibatis;

import it.renren.spilder.dao.ArchivesDAO;
import it.renren.spilder.dataobject.ArchivesDO;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class IbatisArchivesDAO extends SqlMapClientDaoSupport implements ArchivesDAO {

    private static final String Insert_Archives = "Insert_Archives";

    //@Override
    public void insertArchives(ArchivesDO archivesDO) {
        // TODO Auto-generated method stub
        getSqlMapClientTemplate().insert(Insert_Archives, archivesDO);
    }

}
