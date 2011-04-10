package it.renren.spilder.type;

import it.renren.spilder.main.ChildPageDetail;
import it.renren.spilder.main.ParentPage;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.log.Log4j;

import java.util.Iterator;
import java.util.Map;

public class AutoDetectTypes {

    private static Log4j                log4j            = new Log4j(AutoDetectTypes.class.getName());

    /* ���ڴ�ŷ����MAP��������ڳ�ʹ����ʱ�����ʹ������������÷�����ʱ���Ѿ���ֵ�� */
    private static Map<Integer, String> typesMapData     = null;
    private static Iterator<Integer>    typesMapDataKeys = null;
    TypesMap                            typesMap;

    private void init(ParentPage parentPageConfig) {
        try {
            if (typesMapData == null) {
                typesMapData = typesMap.getTypesMap();                
            }
        } catch (Exception e) {
            typesMapData = null;
            log4j.logError(e);
        }
    }

    /**
     * �Զ���⵱ǰ����Ϊʲô���࣬������ܹ������򷵻�Ĭ�ϵķ���
     * 
     * @param parentPageConfig
     * @param detail
     * @return
     */
    public int detectType(ParentPage parentPageConfig, ChildPageDetail detail) {
        int type = -1;
        if (StringUtil.isNull(parentPageConfig.getAutoDetectTypeMapClass())) {
            return Integer.parseInt(parentPageConfig.getDesArticleId());
        }
        init(parentPageConfig);

        String title = detail.getTitle().toLowerCase();
        title = StringUtil.removeHtmlTags(title);
        int currentType = -1;
        typesMapDataKeys = typesMapData.keySet().iterator();
        while (typesMapDataKeys.hasNext()) {/* �ȼ��������Ƿ�����з���ؼ��� */
            currentType = typesMapDataKeys.next();
            if (title.indexOf(typesMapData.get(currentType).toLowerCase()) > 0) {
                type = currentType;
                break;
            }
        }
        if (type == -1) {/* �ټ���������Ƿ�����з���ؼ��� */
            String content = detail.getContent().toLowerCase();
            content = StringUtil.removeHtmlTags(content);
            typesMapDataKeys = typesMapData.keySet().iterator();
            while (typesMapDataKeys.hasNext()) {
                currentType = typesMapDataKeys.next();
                if (content.indexOf(typesMapData.get(currentType).toLowerCase()) > 0) {
                    type = currentType;
                    break;
                }
            }
        }
        if (type == -1) {
            type = Integer.parseInt(parentPageConfig.getDesArticleId());
        }
        return type;
    }

    public void setTypesMap(TypesMap typesMap) {
        this.typesMap = typesMap;
    }
}
