package it.renren.spilder.dao;

import it.renren.spilder.dataobject.ArctinyDO;

public interface ArctinyDAO {

    public Object insertArctiny(ArctinyDO arctinyDO);

    public ArctinyDO selectArctinyByTypeId(ArctinyDO arctinyDO);

    public void updateArctinyTypeidById(ArctinyDO arctinyDO);
}
