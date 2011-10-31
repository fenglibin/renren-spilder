package it.renren.spilder.util;

public class NumberUtil {

    /**
     * 判断给定的字符串是否是数字串
     * 
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (StringUtil.isNull(str)) {
            return Boolean.FALSE;
        }
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char ch = str.charAt(i);
            if (!Character.isDigit(ch)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
}
