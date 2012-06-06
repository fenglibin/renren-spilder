package net.lifegirl.dataobject.dao.appengine;

import java.util.List;

import net.lifegirl.dataobject.bean.ImageType;
import net.lifegirl.dataobject.dao.ImageTypeDAO;

public class AEImageTypeDAO implements ImageTypeDAO {

    @Override
    public void insert(ImageType imageType) {

        PMF.get().getPersistenceManager().makePersistent(imageType);

    }

    @Override
    public List<ImageType> query() {
        String query = "select from " + ImageType.class.getName();
        return (List<ImageType>) PMF.get().getPersistenceManager().newQuery("").execute();
    }
}
