package it.renren.spilder.util.web;

import it.renren.spilder.parser.AHrefElement;
import it.renren.spilder.parser.AHrefParser;
import it.renren.spilder.util.HttpClientUtil;
import it.renren.spilder.util.StringUtil;
import it.renren.spilder.util.UrlUtil;
import it.renren.spilder.util.wash.WashBase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.htmlparser.util.ParserException;

/**
 * @author libinfeng
 */
public class SiteSpilder extends WashBase {

    private static String encode  = "gbk";
    private static String url     = "http://www.open-open.com";
    private static String outPath = "d:/outpath";

    /**
     * @param args
     * @throws IOException
     * @throws HttpException
     * @throws ParserException
     */
    public static void main(String[] args) throws HttpException, IOException, ParserException {
        initArgs(args);
        mkdir(getOutPath());
        analysis(getUrl());
    }

    private static void analysis(String currentPageUrl) throws HttpException, IOException, ParserException {
        // System.out.println("currentPageUrl:"+currentPageUrl);
        String content = HttpClientUtil.getGetResponseWithHttpClient(currentPageUrl, getEncode());
        // write(currentPageUrl, content);
        List<AHrefElement> childLinksList = AHrefParser.ahrefParser(content, getEncode());
        childLinksList = getAbsoluteChildUrlList(currentPageUrl, childLinksList);
        // System.out.println("childUrls:");
        for (AHrefElement link : childLinksList) {
            System.out.println(link.getHref());
            analysis(link.getHref());
        }
    }

    /**
     * @param currentPageUrl
     * @param childLinksList
     * @return
     */
    private static List<AHrefElement> getAbsoluteChildUrlList(String currentPageUrl, List<AHrefElement> childLinksList) {
        List<AHrefElement> element = new ArrayList<AHrefElement>();
        for (AHrefElement link : childLinksList) {
            String childUrl = link.getHref();
            childUrl = UrlUtil.makeUrl(currentPageUrl, childUrl);
            if (!StringUtil.isEmpty(childUrl) && childUrl.startsWith(currentPageUrl)
                && !childUrl.equals(currentPageUrl) && !childUrl.equals(currentPageUrl + "/")) {
                link.setHref(childUrl);
                element.add(link);
            }

        }
        return element;
    }

    private static void mkdir(String dir) {
        File file = new File(dir);
        if (file.exists()) {
            return;
        }
        file.mkdirs();
    }

    private static void write(String url, String content) throws IOException {
        String uri = url.replace(getUrl(), "");
        String outFile = getOutPath() + uri;
        makeParentDir(outFile);
        write2File(content, outFile);
    }

    /**
     * read content from given url
     * 
     * @param url
     * @return
     * @throws IOException
     */
    private static String readUrlContents(String url) throws IOException {
        BufferedReader in = null;
        URL u = new URL(url);
        URLConnection connection = u.openConnection();
        in = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("gbk")));
        StringBuilder sb = new StringBuilder("");
        String inputLine = null;
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        return sb.toString();
    }

    private static void makeParentDir(String outFile) {
        outFile = outFile.replace("\\", "/");
        String[] pathArray = outFile.split("/");
        outFile = outFile.replace(pathArray[pathArray.length - 1], "");
        File file = new File(outFile);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void write2File(String content, String filePath) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        fw.write(content);
        fw.flush();
        fw.close();
    }

    private static String getUrl() {
        String tempUrl = System.getProperty("url");
        if (!StringUtil.isEmpty(tempUrl)) {
            url = tempUrl;
        }
        return url;
    }

    private static String getEncode() {
        String tempEncode = System.getProperty("encode");
        if (!StringUtil.isEmpty(tempEncode)) {
            encode = tempEncode;
        }
        return encode;
    }

    private static String getOutPath() {
        String path = System.getProperty("outPath");
        if (!StringUtil.isEmpty(path)) {
            outPath = path;
        }
        return outPath;
    }

}
