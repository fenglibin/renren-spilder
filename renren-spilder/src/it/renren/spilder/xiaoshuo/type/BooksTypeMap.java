package it.renren.spilder.xiaoshuo.type;

import it.renren.spilder.type.TypesMap;
import it.renren.spilder.util.log.Log4j;
import it.renren.spilder.xiaoshuo.dao.TypesDAO;
import it.renren.spilder.xiaoshuo.dataobject.Types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooksTypeMap implements TypesMap {

    private static Log4j                log4j = new Log4j(BooksTypeMap.class.getName());
    private static Map<Integer, String> types = null;
    TypesDAO                            typesDAO;

    public Map<Integer, String> getTypesMap() {
        if (types == null || types.size() == 0) {
            types = new HashMap<Integer, String>();
        } else {
            return types;
        }
        try {
            List<Types> typesList = typesDAO.selectAllTypes();
            if (typesList != null && !typesList.isEmpty()) {
                for (Types type : typesList) {
                    types.put(type.getId(), type.getName());
                }
            }
        } catch (Exception e) {
            log4j.logError("Get all types error happened.", e);
        }
        return types;
    }

    public void setTypesDAO(TypesDAO typesDAO) {
        this.typesDAO = typesDAO;
    }

}
