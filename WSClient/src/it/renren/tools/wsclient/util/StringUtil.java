package it.renren.tools.wsclient.util;

/**
 * @author libinfeng
 */
public class StringUtil {

    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

}
