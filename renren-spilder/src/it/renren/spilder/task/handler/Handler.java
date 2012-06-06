package it.renren.spilder.task.handler;

import it.renren.spilder.main.detail.ChildPageDetail;

/**
 * 对内容进行处理的Handler，针对不同的内容进行不同的处理
 * 
 * @author Administrator
 */
public interface Handler {

    public void execute(ChildPageDetail detail);
}
