package it.renren.spilder.xiaoshuo.dao.ibatis;

import it.renren.spilder.dataobject.DownurlDO;
import it.renren.spilder.xiaoshuo.dao.DownurlDAO;
import it.renren.spilder.xiaoshuo.dataobject.Downurl;

import java.util.Date;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class DownurlDAOImpl extends SqlMapClientDaoSupport implements DownurlDAO {

    public DownurlDAOImpl(){
        super();
    }

    public Downurl selectDownurl(String url) {
        Downurl key = new Downurl();
        key.setUrl(url);
        Downurl record = (Downurl) getSqlMapClientTemplate().queryForObject("downurl.selectByPrimaryKey", key);
        return record;
    }

    @Override
    public void insertDownurl(DownurlDO downurlDO) {
        Downurl url = (Downurl) downurlDO;
        url.setIntime(new Date());
        getSqlMapClientTemplate().insert("downurl.insert", downurlDO);

    }

}
