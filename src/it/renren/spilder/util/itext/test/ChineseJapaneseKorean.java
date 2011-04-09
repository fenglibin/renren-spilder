package it.renren.spilder.util.itext.test;

import it.renren.spilder.util.log.Log4j;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Using CJK Fonts.
 */
public class ChineseJapaneseKorean {

    private static Log4j log4j = new Log4j(ChineseJapaneseKorean.class.getName());

    /**
     * Using CJK fonts
     * 
     * @param args no arguments needed
     */
    public static void main(String[] args) {
        log4j.logDebug("CJK Fonts");

        // step 1: creation of a document-object
        Document document = new Document();
        try {

            // step 2: creation of the writer
            PdfWriter.getInstance(document, new FileOutputStream("cjk.pdf"));

            // step 3: we open the document
            document.open();
            String chinese = "PDFBox是Java实现的PDF文档协作类库，<br>提供PDF文档的创建、处理以及文档内容提取功能，也包含了一些命令行实用工具";

            // step 4: we add content to the document
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            // BaseFont bfChinese = BaseFont.createFont("c:\\windows\\fonts\\simsun.ttc,1", BaseFont.IDENTITY_H,
            // BaseFont.NOT_EMBEDDED);
            Font FontChinese = new Font(bfChinese, 12, Font.NORMAL);
            Paragraph p = new Paragraph(chinese, FontChinese);

            document.add(p);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }

        // step 5: we close the document
        document.close();
    }
}
