package it.renren.spilder.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * @author Fenglibin
 * @E-mail:56553655@163.com
 * @blog :blog.csdn.net/fenglibing
 * @site :www.6666-6666.com
 * @version 1.0 ����ʱ�䣺2009-11-11 ����02:52:34 ��˵��:�Դ������ҳ���ݣ����е�����ͼƬ���н�������������ͼƬ
 */
public class ImageParser {

    /**
     * ��ȡ������ҳ����������ͼƬ�ĵ�ַ����ImageElement��List����
     * 
     * @param content ����ָ��URL��ȡ������
     * @return ����ͼƬ��ɵ�ImageElement�Ķ���List
     * @throws ParserException
     */
    public static List<ImageElement> imageParser(String content, String charset) throws ParserException {
        List<ImageElement> ret = new ArrayList<ImageElement>();
        Parser myParser = null;
        NodeList nodeList = null;
        myParser = Parser.createParser(content, charset);
        NodeFilter imageFilter = new NodeClassFilter(ImageTag.class);
        OrFilter lastFilter = new OrFilter();
        lastFilter.setPredicates(new NodeFilter[] { imageFilter });
        Map<String, String> imageSrc = new HashMap<String, String>();

        nodeList = myParser.parse(lastFilter);
        Node[] nodes = nodeList.toNodeArray();
        for (int i = 0; i < nodes.length; i++) {
            Node anode = (Node) nodes[i];
            ImageElement fe = new ImageElement();
            if (anode instanceof ImageTag) {// ��������ͼƬ������������ͼƬ
                ImageTag imageNode = (ImageTag) anode;
                if (imageSrc.get(imageNode.getAttribute("src")) == null) {
                    fe.setSrc(imageNode.getAttribute("src"));
                    fe.setAlt(imageNode.getAttribute("alt"));
                    ret.add(fe);
                    imageSrc.put(imageNode.getAttribute("src"), imageNode.getAttribute("src"));
                }
            }
        }
        return ret;
    }
}
