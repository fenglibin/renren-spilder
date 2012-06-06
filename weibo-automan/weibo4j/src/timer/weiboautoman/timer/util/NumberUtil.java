package weiboautoman.timer.util;

public class NumberUtil {

    public static long getPages(long totalCount, int pageSize) {
        return totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
    }

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

    /**
     * 获取1-1000以内的随机数
     * 
     * @return
     */
    public static int getRandomInt() {
        return (int) (Math.random() * 1000);
    }
}
