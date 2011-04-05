package it.renren.spilder.util.itext.test;

import it.renren.spilder.util.log.Log4j;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;

import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

public class HtmliText {
	private static Log4j log4j = new Log4j(HtmliText.class.getName());
	public static void main(String[] args) {
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		try {
			BaseFont bfChinese = BaseFont.createFont("STSongStd-Light",	"UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			Font fontChinese = new Font(bfChinese, 12, Font.NORMAL);
			StyleSheet st = new StyleSheet();
			st.loadTagStyle("body", "leading", "19,0");
			PdfWriter.getInstance(document, new FileOutputStream("html2.pdf"),document);
			document.open();
			ArrayList p = HTMLWorker.parseToList(new FileReader("e:/t.html"),st);
			log4j.logDebug("arraylist----->" + p);
			Paragraph par = new Paragraph("ฮารว", fontChinese);
			document.add(par);
			for (int k = 0; k < p.size(); ++k){
				Element e = (Element)p.get(k);
				document.add(e);
			}
			document.close();

		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
	}

}
