package it.renren.spilder.type;

import it.renren.spilder.dao.ArctypeDAO;
import it.renren.spilder.dataobject.ArctypeDO;
import it.renren.spilder.util.log.Log4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DedecmsTypesMap implements TypesMap {

    private static Log4j                log4j = new Log4j(DedecmsTypesMap.class.getName());
    private static Map<Integer, String> types = null;
    ArctypeDAO                          arctypeDAO;

    public Map<Integer, String> getTypesMap() {
        if (types == null) {
            types = new HashMap<Integer, String>();
        } else {
            return types;
        }
        try {
            List<ArctypeDO> arctypeDOList = arctypeDAO.getArctypeList();
            for (ArctypeDO arctypeDO : arctypeDOList) {
                types.put(arctypeDO.getId(), arctypeDO.getTypename());
            }
        } catch (Exception e) {
            log4j.logError(e);
        }
        return types;
    }

    public void setArctypeDAO(ArctypeDAO arctypeDAO) {
        this.arctypeDAO = arctypeDAO;
    }
}
