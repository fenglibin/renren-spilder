package it.renren.spilder.type;

import it.renren.spilder.main.config.ParentPage;
import it.renren.spilder.main.detail.ChildPageDetail;
import it.renren.spilder.util.StringUtil;

import java.util.Iterator;
import java.util.Map;

public class AutoDetectTypes {

    /* ���ڴ�ŷ����MAP��������ڳ�ʹ����ʱ�����ʹ������������÷�����ʱ���Ѿ���ֵ�� */
    private static Map<Integer, String> typesMapData = null;
    TypesMap                            typesMap;

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
        if (typesMapData == null) {
            typesMapData = typesMap.getTypesMap();
        }

        String title = detail.getTitle().toLowerCase();
        title = StringUtil.removeHtmlTags(title);
        int currentType = -1;
        Iterator<Integer> typesMapDataKeys = typesMapData.keySet().iterator();
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
