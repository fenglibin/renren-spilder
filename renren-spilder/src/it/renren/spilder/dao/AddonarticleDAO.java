package it.renren.spilder.dao;

import it.renren.spilder.dataobject.AddonarticleDO;

import java.util.List;

public interface AddonarticleDAO {

    public void insertAddonarticle(AddonarticleDO addonarticleDO);

    public AddonarticleDO selectBodyByAid(int aid);

    public List<AddonarticleDO> selectByPagesize(int start, int pageSize);

    public void updateBodyByAid(AddonarticleDO addonarticleDO);

    public int getTotalRecords();
}
