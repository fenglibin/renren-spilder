package it.renren.spilder.xiaoshuo.type;

import it.renren.spilder.main.config.ParentPage;
import it.renren.spilder.main.detail.ChildPageDetail;
import it.renren.spilder.type.Type;
import it.renren.spilder.type.TypesMap;
import it.renren.spilder.util.StringUtil;

import java.util.Map;
import java.util.Map.Entry;

public class GetBooksType implements Type {

    /* ���ڴ�ŷ����MAP��������ڳ�ʹ����ʱ�����ʹ������������÷�����ʱ���Ѿ���ֵ�� */
    private static Map<Integer, String> typesMapData = null;
    TypesMap                            typesMap;

    /**
     * �Զ���⵱ǰ����Ϊʲô���࣬������ܹ������򷵻��������
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
        String typeBefore = "<p><strong>����: </strong><span>";
        String typeEnd = "</span></p>";
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
            type = (int) Math.random() * typesMapData.size();
        }
        return type;
    }

    public void setTypesMap(TypesMap typesMap) {
        this.typesMap = typesMap;
    }

}
