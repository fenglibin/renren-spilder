package it.renren.spilder.xiaoshuo.type;

import it.renren.spilder.main.config.ParentPage;
import it.renren.spilder.main.detail.ChildPageDetail;
import it.renren.spilder.type.Type;
import it.renren.spilder.type.TypesMap;
import it.renren.spilder.util.StringUtil;

import java.util.Map;
import java.util.Map.Entry;

public class GetBooksType implements Type {

    /* 用于存放分类的MAP，程序会在初使化的时候将其初使化，到这里调用方法的时候已经有值了 */
    private static Map<Integer, String> typesMapData = null;
    TypesMap                            typesMap;

    /**
     * 自动检测当前文章为什么分类，如果不能够检测出则返回随机分类
     * 
     * @param parentPageConfig
     * @param detail
     * @return
     */
    public int getType(ParentPage parentPageConfig, ChildPageDetail detail) {
        int type = -1;
        if (typesMapData == null || typesMapData.size() == 0) {
            typesMapData = typesMap.getTypesMap();
        }
        String typeBefore = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"><title>";
        String typeEnd = " - ";
        String typeName = StringUtil.subString(detail.getOriginalContent(), typeBefore, typeEnd);
        if (!StringUtil.isEmpty(typeName)) {
            for (Entry<Integer, String> entry : typesMapData.entrySet()) {
                if (typeName.indexOf(entry.getValue()) >= 0) {
                    type = entry.getKey();
                    break;
                }
            }
        }
        if (type == -1) {
            int index = (int) Math.random() * typesMapData.size();
            int tempIndex = 0;
            for (Entry<Integer, String> entry : typesMapData.entrySet()) {
                if (tempIndex == index) {
                    type = entry.getKey();
                    break;
                }
                tempIndex++;
            }
        }
        return type;
    }

    public void setTypesMap(TypesMap typesMap) {
        this.typesMap = typesMap;
    }

}
