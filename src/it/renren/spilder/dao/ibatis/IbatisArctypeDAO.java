package it.renren.spilder.dao.ibatis;

import java.util.List;

import it.renren.spilder.dao.ArctypeDAO;
import it.renren.spilder.dataobject.ArctypeDO;
import it.renren.spilder.util.StringUtil;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class IbatisArctypeDAO extends SqlMapClientDaoSupport implements
		ArctypeDAO {
	private static final String Select_All_Arctype = "Select_All_Arctype";
	//������׺������֧�ֶ����ͬ�ı�Ĳ�ѯ
    private String conditionSuffix;
	@Override
	public List<ArctypeDO> getArctypeList() {
		String statementName = Select_All_Arctype;
		if(!StringUtil.isNull(conditionSuffix)){
			statementName = statementName + conditionSuffix;
		}
		return getSqlMapClientTemplate().queryForList(statementName);
	}
	public void setConditionSuffix(String conditionSuffix) {
		this.conditionSuffix = conditionSuffix;
	}

}
