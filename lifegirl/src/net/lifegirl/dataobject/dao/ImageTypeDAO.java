package net.lifegirl.dataobject.dao;

import java.util.List;

import net.lifegirl.dataobject.bean.ImageType;

/**
 * 类ImageTypeDAO.java的实现描述：TODO 类实现描述
 * 
 * @author Administrator 2011-10-19 下午09:46:14
 */
public interface ImageTypeDAO {

    /**
     * 增加一种类型
     * 
     * @param imageType
     */
    public void insert(ImageType imageType);

    /**
     * 查询所有的类型
     * 
     * @return
     */
    public List<ImageType> query();
}
