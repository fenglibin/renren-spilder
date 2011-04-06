package it.renren.spilder.dao.ibatis;

import it.renren.spilder.dao.AddonarticleDAO;
import it.renren.spilder.dataobject.AddonarticleDO;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class IbatisAddonarticleDAO extends SqlMapClientDaoSupport implements AddonarticleDAO {

    private static final String Insert_Addonarticle = "Insert_Addonarticle";

    @Override
    public void insertAddonarticle(AddonarticleDO addonarticleDO) {
        getSqlMapClientTemplate().insert(Insert_Addonarticle, addonarticleDO);

    }

}
