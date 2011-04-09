package it.renren.spilder.dao.ibatis;

import it.renren.spilder.dao.AddonarticleDAO;
import it.renren.spilder.dataobject.AddonarticleDO;
import it.renren.spilder.util.StringUtil;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class IbatisAddonarticleDAO extends SqlMapClientDaoSupport implements AddonarticleDAO {

    private static final String Insert_Addonarticle = "Insert_Addonarticle";
    // ������׺������֧�ֶ����ͬ�ı�Ĳ�ѯ
    private String              conditionSuffix;

    @Override
    public void insertAddonarticle(AddonarticleDO addonarticleDO) {
        String statementName = Insert_Addonarticle;
        if (!StringUtil.isNull(conditionSuffix)) {
            statementName = statementName + conditionSuffix;
        }
        getSqlMapClientTemplate().insert(statementName, addonarticleDO);

    }

    public void setConditionSuffix(String conditionSuffix) {
        this.conditionSuffix = conditionSuffix;
    }

}
