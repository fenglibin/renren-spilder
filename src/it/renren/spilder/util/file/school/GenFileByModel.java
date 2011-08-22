package it.renren.spilder.util.file.school;

import it.renren.spilder.parser.AHrefElement;
import it.renren.spilder.parser.AHrefParser;
import it.renren.spilder.util.FileUtil;
import it.renren.spilder.util.HttpClientUtil;
import it.renren.spilder.util.StringUtil;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.htmlparser.util.ParserException;

/**
 * ��ǰ�ļ�ֻ�ʺϴ���http://linux.chinaitlab.com/special/linuxcom/Index.html ��GenFileByModel.java��ʵ��������TODO ��ʵ������
 * 
 * @author fenglibin 2011-8-22 ����10:44:35
 */
public class GenFileByModel {

    private static String charset     = "gbk";
    private static String pre         = "<li class='pre'><a href='commandPre.html'>Previous Page</a></li>";
    private static String next        = "<li class='next'><a href='commandNext.html'>Next Page</a></li>";
    private static String commandFile = "model/school/linux/commandName.html";
    private static String modelFile   = "model/school/linux/command_model.html";
    // ����������ļ���ÿ������һ��
    private static String command     = "model/school/linux/command.txt";
    static List<String>   commandList = null;
    static {
        try {
            commandList = FileUtil.getFile2List(command);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param args
     * @throws IOException
     * @throws ParserException
     */
    public static void main(String[] args) throws IOException, ParserException {
        genModelFile();
        getContent();
    }

    /**
     * ����ģ���ļ�
     * 
     * @throws IOException
     */
    private static void genModelFile() throws IOException {
        String modelContent = FileUtil.getFileContent(modelFile, charset);
        int index = 0;
        for (String oneCommand : commandList) {
            String oneCommandPre = pre;
            String oneCommandNext = next;
            String oneCommandFile = commandFile;
            String oneCommandContent = modelContent;
            String preCommand = "";
            String nextCommand = "";
            if (index == 0) {// ��һ��
                oneCommandPre = "";
                nextCommand = commandList.get(index + 1);
            } else if (index == commandList.size() - 1) {// ���һ��
                preCommand = commandList.get(index - 1);
                oneCommandNext = "";
            } else {
                preCommand = commandList.get(index - 1);
                nextCommand = commandList.get(index + 1);
            }
            oneCommandPre = oneCommandPre.replace("commandPre", preCommand);
            oneCommandNext = oneCommandNext.replace("commandNext", nextCommand);
            oneCommandContent = oneCommandContent.replace("#pre#", oneCommandPre);
            oneCommandContent = oneCommandContent.replace("#next#", oneCommandNext);
            oneCommandFile = oneCommandFile.replace("commandName", oneCommand);
            FileUtil.writeFile(oneCommandFile, oneCommandContent, charset);
            index++;
        }
    }

    /**
     * ��ȡÿ�����������
     * 
     * @throws HttpException
     * @throws IOException
     * @throws ParserException
     */
    private static void getContent() throws HttpException, IOException, ParserException {
        String commandUrl = "http://linux.chinaitlab.com/special/linuxcom/Index.html";
        String mainStart = "</font></span></div></td>";
        String mainEnd = "����ר��</td>";
        String childStart = "<div class=Article_content>";
        String childEnd = "<div align=right style=\"margin:3px 0;\">";
        String contentReplace = "<div class=\"STYLE1\"><a href=\"http://bbs.chinaitlab.com\" target=\"_blank\" class=\"STYLE1\">��ӭ����Linux������̳����200������Ա��������  >>����</a></div>";
        String mainContent = HttpClientUtil.getGetResponseWithHttpClient(commandUrl, charset);
        mainContent = StringUtil.subString(mainContent, mainStart, mainEnd);
        List<AHrefElement> childLinks = AHrefParser.ahrefParser(mainContent, charset);
        for (String oneCommand : commandList) {
            String oneCommandFile = commandFile;
            oneCommandFile = oneCommandFile.replace("commandName", oneCommand);
            String oneCommandContent = FileUtil.getFileContent(oneCommandFile, charset);
            String content = "";
            for (AHrefElement href : childLinks) {
                if (href.getHrefText().equals(oneCommand)) {
                    content = HttpClientUtil.getGetResponseWithHttpClient(href.getHref(), charset);
                    content = StringUtil.subString(content, childStart, childEnd);
                    content = content.replace(contentReplace, "");
                    break;
                }
            }
            oneCommandContent = oneCommandContent.replace("#content#", content);
            FileUtil.writeFile(oneCommandFile, oneCommandContent, charset);
        }
    }
}
