package net.lifegirl.dataobject.dao;

import java.util.List;

import net.lifegirl.dataobject.bean.ImageType;

/**
 * ��ImageTypeDAO.java��ʵ��������TODO ��ʵ������
 * 
 * @author Administrator 2011-10-19 ����09:46:14
 */
public interface ImageTypeDAO {

    /**
     * ����һ������
     * 
     * @param imageType
     */
    public void insert(ImageType imageType);

    /**
     * ��ѯ���е�����
     * 
     * @return
     */
    public List<ImageType> query();
}
