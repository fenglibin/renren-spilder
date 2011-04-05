package it.renren.spilder.task;

import it.renren.spilder.main.ChildPage;
import it.renren.spilder.main.ChildPageDetail;
import it.renren.spilder.main.ParentPage;

public interface Task {
	public void doTask(ParentPage parentPageConfig,ChildPage childPageConfig,ChildPageDetail detail) throws Exception;
	public void closeTask();
}
