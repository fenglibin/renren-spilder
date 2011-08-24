package it.renren.spilder.dao;

import it.renren.spilder.dataobject.AddonarticleDO;

public interface AddonarticleDAO {

    public void insertAddonarticle(AddonarticleDO addonarticleDO);

    public AddonarticleDO selectBodyByAid(int aid);

    public void updateBodyByAid(AddonarticleDO addonarticleDO);
}
