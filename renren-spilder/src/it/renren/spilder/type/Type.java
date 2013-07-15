package it.renren.spilder.type;

import it.renren.spilder.main.config.ParentPage;
import it.renren.spilder.main.detail.ChildPageDetail;

public interface Type {

    public int getType(ParentPage parentPageConfig, ChildPageDetail detail);
}
