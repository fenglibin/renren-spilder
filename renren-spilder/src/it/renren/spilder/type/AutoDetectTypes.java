package it.renren.spilder.type;

import it.renren.spilder.main.config.ParentPage;
import it.renren.spilder.main.detail.ChildPageDetail;
import it.renren.spilder.util.StringUtil;

import java.util.Iterator;
import java.util.Map;

public class AutoDetectTypes {

    /* 用于存放分类的MAP，程序会在初使化的时候将其初使化，到这里调用方法的时候已经有值了 */
    private static Map<Integer, String> typesMapData = null;
    TypesMap                            typesMap;

    /**
     * 自动检测当前文章为什么分类，如果不能够检测出则返回默认的分类
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
        while (typesMapDataKeys.hasNext()) {/* 先检测标题中是否包括有分类关键字 */
            currentType = typesMapDataKeys.next();
            if (title.indexOf(typesMapData.get(currentType).toLowerCase()) > 0) {
                type = currentType;
                break;
            }
        }
        if (type == -1) {/* 再检测内容中是否包括有分类关键字 */
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
