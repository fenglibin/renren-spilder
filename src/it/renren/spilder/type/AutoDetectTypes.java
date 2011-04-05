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

	/* 用于存放分类的MAP，程序会在初使化的时候将其初使化，到这里调用方法的时候已经有值了 */
	public static Map<Integer, String> typesMap = null;

	private static List<String> typesList = null;

	/* 因为同时要插入繁体和简体两种，如果繁体或简单确定了分类，且因为是同一篇文章，就不必再确定了，直接使用前面的分类结果即可，但是要确保最后处理的，要将该值重置为-1 */
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
	 * 自动检测当前文章为什么分类，如果不能够检测出则返回默认的分类
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
		while (keys.hasNext()) {/* 先检测标题中是否包括有分类关键字 */
			currentType = keys.next();
			if (title.indexOf(typesMap.get(currentType).toLowerCase()) > 0) {
				type = currentType;
				break;
			}
		}
		if (type == -1) {/* 再检测内容中是否包括有分类关键字 */
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
