package it.renren.spilder.dao.ibatis;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import it.renren.spilder.dao.ArctinyDAO;
import it.renren.spilder.dataobject.ArctinyDO;

public class IbatisArctinyDAO extends SqlMapClientDaoSupport implements ArctinyDAO {

    private static final String Insert_Arctiny           = "Insert-Arctiny";
    private static final String Select_Current_ArticleId = "Select_Current_ArticleId";
    private static final String Update_Current_ArticleId = "Update_Current_ArticleId";

    //@Override
    public void insertArctiny(ArctinyDO arctinyDO) {
        getSqlMapClientTemplate().insert(Insert_Arctiny, arctinyDO);
    }

    //@Override
    public ArctinyDO selectArctinyByTypeId(ArctinyDO arctinyDO) {
        return (ArctinyDO) getSqlMapClientTemplate().queryForObject(Select_Current_ArticleId, arctinyDO);
    }

    //@Override
    public void updateArctinyTypeidById(ArctinyDO arctinyDO) {
        getSqlMapClientTemplate().update(Update_Current_ArticleId, arctinyDO);
    }
}
