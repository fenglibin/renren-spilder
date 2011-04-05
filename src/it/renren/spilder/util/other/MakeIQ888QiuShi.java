package it.renren.spilder.util.other;

import java.util.List;

import it.renren.spilder.parser.AHrefElement;
import it.renren.spilder.parser.AHrefParser;
import it.renren.spilder.util.FileUtil;
import it.renren.spilder.util.HttpClientUtil;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.log.Log4j;

/**
 * 根据IQ888.COM的QIUSHI页面(http://www.iq888.com/qiushi/choushi.htm)，根据关键字，生成所有配置文件
 * @author Administrator
 *
 */
public class MakeIQ888QiuShi {
	private static Log4j log4j = new Log4j(MakeIQ888QiuShi.class.getName());
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String getHtml = "http://www.iq888.com/qiushi/choushi.htm";
		String filename = "E:/work/mywork/renren-spilder/config/xiaohua/qiushi/rule_iq888_qiushi_#.xml";
		String modXmlFile = "E:/work/mywork/renren-spilder/config/xiaohua/qiushi/rule_iq888_qiushi_model.xml";
		String charset = "gbk";
		String content = "";

		content = HttpClientUtil.getGetResponseWithHttpClient(getHtml,charset);
		content = StringUtil.subString(content,"<div class=\"intro\" style=\"overflow: hidden; font-size:small\">","</div>");
		try {
			String modelContent = FileUtil.getFileContent(modXmlFile);
			List<AHrefElement> childLinks = AHrefParser.ahrefParser(content, "", "",charset,false);
			int index=1;
			for (AHrefElement link : childLinks) {
				try{
					String linkText = link.getHrefText();
					String thisfilename = filename.replace("#", String.valueOf(index));
					String thisContent = modelContent.replace("#keywords#", linkText);
					FileUtil.writeFile(thisfilename, thisContent);
					log4j.logDebug(link.getHrefText());
					index++;
				}catch(Exception e){
					log4j.logError(e);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log4j.logError(e);
		}

	}

}
