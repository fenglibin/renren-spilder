package it.renren.spilder.dao.ibatis;

import it.renren.spilder.dao.AddonarticleDAO;
import it.renren.spilder.dataobject.AddonarticleDO;
import it.renren.spilder.dataobject.Page;
import it.renren.spilder.util.StringUtil;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class IbatisAddonarticleDAO extends SqlMapClientDaoSupport implements AddonarticleDAO {

    private static final String Insert_Addonarticle = "Insert_Addonarticle";
    private static final String SELECT_BODY_BY_AID  = "SELECT_BODY_BY_AID";
    private static final String UPDATE_BODY_BY_AID  = "UPDATE_BODY_BY_AID";
    private static final String SELECT_BY_PAGESIZE  = "SELECT_BY_PAGESIZE";
    private static final String GET_TOTAL_RECORDS   = "GET_TOTAL_RECORDS";
    // 条件后缀，用于支持多个不同的表的查询
    private String              tablePrefix;

    @Override
    public void insertAddonarticle(AddonarticleDO addonarticleDO) {
        addonarticleDO.setTablePrefix(StringUtil.getTablePrefix(tablePrefix));
        getSqlMapClientTemplate().insert(Insert_Addonarticle, addonarticleDO);

    }

    @Override
    public AddonarticleDO selectBodyByAid(int aid) {
        AddonarticleDO addonarticleDO = new AddonarticleDO();
        addonarticleDO.setAid(aid);
        addonarticleDO.setTablePrefix(StringUtil.getTablePrefix(tablePrefix));
        return (AddonarticleDO) getSqlMapClientTemplate().queryForObject(SELECT_BODY_BY_AID, addonarticleDO);
    }

    @Override
    public void updateBodyByAid(AddonarticleDO addonarticleDO) {
        addonarticleDO.setTablePrefix(StringUtil.getTablePrefix(tablePrefix));
        getSqlMapClientTemplate().update(UPDATE_BODY_BY_AID, addonarticleDO);
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    @Override
    public List<AddonarticleDO> selectByPagesize(int start, int pageSize) {
        Page page = new Page();
        page.setPageSize(pageSize);
        page.setStart(start);
        page.setTablePrefix(StringUtil.getTablePrefix(tablePrefix));
        return getSqlMapClientTemplate().queryForList(SELECT_BY_PAGESIZE, page);
    }

    @Override
    public int getTotalRecords() {
        AddonarticleDO addonarticleDO = new AddonarticleDO();
        addonarticleDO.setTablePrefix(StringUtil.getTablePrefix(tablePrefix));
        return (Integer) getSqlMapClientTemplate().queryForObject(GET_TOTAL_RECORDS, addonarticleDO);
    }

}
