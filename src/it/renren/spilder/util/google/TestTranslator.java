package it.renren.spilder.util.google;

import it.renren.spilder.util.log.Log4j;

import com.google.api.translate.Language;
import com.google.api.translate.Translate;

public class TestTranslator {

    private static Log4j log4j = new Log4j(TestTranslator.class.getName());

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            Translate.setHttpReferrer("http://www.google.com");
            // String translatedText = Translate.translate("How are you?",Language.ENGLISH,
            // Language.CHINESE_SIMPLIFIED);
            String str = "";
            String translatedText = Translate.translate(str, Language.CHINESE_SIMPLIFIED, Language.ENGLISH);
            log4j.logDebug(translatedText);
        } catch (Exception ex) {
            log4j.logError(ex);
        }

    }
}
