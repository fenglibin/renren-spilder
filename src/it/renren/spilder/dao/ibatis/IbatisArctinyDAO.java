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
    private String              tablePrefix;

    @Override
    public Object insertArctiny(ArctinyDO arctinyDO) {
        arctinyDO.setTablePrefix(StringUtil.getTablePrefix(tablePrefix));
        return getSqlMapClientTemplate().insert(Insert_Arctiny, arctinyDO);
    }

    @Override
    public ArctinyDO selectArctinyByTypeId(ArctinyDO arctinyDO) {
        arctinyDO.setTablePrefix(StringUtil.getTablePrefix(tablePrefix));
        return (ArctinyDO) getSqlMapClientTemplate().queryForObject(Select_Current_ArticleId, arctinyDO);
    }

    @Override
    public void updateArctinyTypeidById(ArctinyDO arctinyDO) {
        arctinyDO.setTablePrefix(StringUtil.getTablePrefix(tablePrefix));
        getSqlMapClientTemplate().update(Update_Current_ArticleId, arctinyDO);
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }
}
