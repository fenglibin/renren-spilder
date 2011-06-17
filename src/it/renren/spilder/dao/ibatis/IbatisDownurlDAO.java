package it.renren.spilder.dao.ibatis;

import it.renren.spilder.dao.DownurlDAO;
import it.renren.spilder.dataobject.DownurlDO;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class IbatisDownurlDAO extends SqlMapClientDaoSupport implements DownurlDAO {

    private static final String Insert_DOWNURL = "Insert_DOWNURL";
    private static final String SELECT_DOWNURL = "SELECT_DOWNURL";

    @Override
    public void insertDownurl(DownurlDO downurlDO) {
        getSqlMapClientTemplate().insert(Insert_DOWNURL, downurlDO);

    }

    @Override
    public DownurlDO selectDownurl(String url) {
        DownurlDO downurlDO = new DownurlDO();
        downurlDO.setUrl(url);
        return (DownurlDO) getSqlMapClientTemplate().queryForObject(SELECT_DOWNURL, downurlDO);
    }

}
