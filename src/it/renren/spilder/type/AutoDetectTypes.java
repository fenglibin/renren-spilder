package it.renren.spilder.type;

import it.renren.spilder.main.ChildPageDetail;
import it.renren.spilder.main.ParentPage;
import it.renren.spilder.util.log.Log4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AutoDetectTypes {
	private static Log4j log4j = new Log4j(AutoDetectTypes.class.getName());

	/* ���ڴ�ŷ����MAP��������ڳ�ʹ����ʱ�����ʹ������������÷�����ʱ���Ѿ���ֵ�� */
	public static Map<Integer, String> typesMap = null;

	private static List<String> typesList = null;

	/* ��ΪͬʱҪ���뷱��ͼ������֣����������ȷ���˷��࣬����Ϊ��ͬһƪ���£��Ͳ�����ȷ���ˣ�ֱ��ʹ��ǰ��ķ��������ɣ�����Ҫȷ�������ģ�Ҫ����ֵ����Ϊ-1 */
	public static int currentTypeId = -1;

	public static void init(ParentPage parentPageConfig) {
		TypesMap types;
		try {
			types = (TypesMap) Class.forName(
					parentPageConfig.getAutoDetectTypeMapClass()).newInstance();
			if (typesMap == null) {
				typesMap = types.getTypesMap(parentPageConfig);
			}
			typesMap2typesList();
		} catch (Exception e) {
			typesMap = null;
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
	public static int detectType(ParentPage parentPageConfig,
			ChildPageDetail detail) {
		int type = -1;
		if (typesMap == null) {
			return Integer.parseInt(parentPageConfig.getDesArticleId());
		}
		if (currentTypeId != -1) {
			return currentTypeId;
		}
		Iterator<Integer> keys = typesMap.keySet().iterator();
		String title = detail.getTitle().toLowerCase();
		int currentType = -1;
		while (keys.hasNext()) {/* �ȼ��������Ƿ�����з���ؼ��� */
			currentType = keys.next();
			if (title.indexOf(typesMap.get(currentType).toLowerCase()) > 0) {
				type = currentType;
				break;
			}
		}
		if (type == -1) {/* �ټ���������Ƿ�����з���ؼ��� */
			String content = detail.getContent().toLowerCase();
			keys = typesMap.keySet().iterator();
			while (keys.hasNext()) {
				currentType = keys.next();
				if (content.indexOf(typesMap.get(currentType).toLowerCase()) > 0) {
					type = currentType;
					break;
				}
			}
		}
		if (type == -1) {
			type = Integer.parseInt(parentPageConfig.getDesArticleId());
		}
		currentTypeId = type;
		return type;
	}

	private static void typesMap2typesList() {
		if (typesList != null) {
			return;
		}
		typesList = new ArrayList<String>();
		Iterator<Integer> it = typesMap.keySet().iterator();
		while (it.hasNext()) {
			typesList.add(typesMap.get(it.next()));
		}
	}

	public static void resetCurrentTypeId() {
		currentTypeId = -1;
	}
}
