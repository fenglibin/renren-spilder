package it.renren.spilder.task.handler;

import it.renren.spilder.main.ChildPageDetail;

public class TudouContentHandler implements Handler{

	public void execute(ChildPageDetail detail) {
		String content = detail.getContent().replace("programs/view", "v");
		content = content + "v.swf";
		content = "<embed src=\"" + content+"\" type=\"application/x-shockwave-flash\" allowscriptaccess=\"always\" allowfullscreen=\"true\" wmode=\"opaque\" width=\"480\" height=\"400\"></embed>";
		detail.setContent(content);
	}

}
