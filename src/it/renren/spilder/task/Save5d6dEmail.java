package it.renren.spilder.task;

import it.renren.spilder.main.config.ChildPage;
import it.renren.spilder.main.config.ParentPage;
import it.renren.spilder.main.detail.ChildPageDetail;
import it.renren.spilder.util.FileUtil;

public class Save5d6dEmail extends Task {

    @Override
    public void doTask(ParentPage parentPageConfig, ChildPage childPageConfig, ChildPageDetail detail) throws Exception {
        String email = detail.getContent();
        String saveFile = "email.txt";
        if (email.indexOf("@") > 0) {
            FileUtil.writeFileAppend(saveFile, email);
        }

    }

    @Override
    protected int getDealedArticleNum() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isDealed(String url) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void saveDownUrl(String url) {
        // TODO Auto-generated method stub

    }

}
