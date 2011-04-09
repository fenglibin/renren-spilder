package it.renren.spilder.parser;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class MetaParser {

    public static String getMetaContent(String content, String charset, String metaname) throws ParserException {
        String metaContent = "";
        Parser myParser = Parser.createParser(content, charset);
        NodeFilter imageFilter = new NodeClassFilter(MetaTag.class);
        OrFilter lastFilter = new OrFilter();
        lastFilter.setPredicates(new NodeFilter[] { imageFilter });
        NodeList nodeList = myParser.parse(lastFilter);
        Node[] nodes = nodeList.toNodeArray();
        for (int i = 0; i < nodes.length; i++) {
            MetaTag anode = (MetaTag) nodes[i];
            String name = anode.getMetaTagName();
            if (name != null && name.equalsIgnoreCase(metaname)) {
                metaContent = anode.getAttribute("content");
                break;
            }
        }
        return metaContent.trim();
    }
}
