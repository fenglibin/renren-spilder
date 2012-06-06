package weiboautoman.timer.util;

/**
 * 类CodeUtil.java的实现描述：Native2Ascii和Ascii2Native的Java实现(中文转ascii)
 * 
 * @author Administrator 2011-10-7 下午09:08:28
 */
public class CodeUtil {

    /**
     * prefix of ascii string of native character
     */
    private static String PREFIX = "\\u";

    /**
     * Native to ascii string. It's same as execut native2ascii.exe.
     * 
     * @param str native string
     * @return ascii string
     */
    public static String native2Ascii(String str) {
        char[] chars = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            sb.append(char2Ascii(chars[i]));
        }
        return sb.toString();
    }

    /**
     * Native character to ascii string.
     * 
     * @param c native character
     * @return ascii string
     */
    private static String char2Ascii(char c) {
        if (c > 255) {
            StringBuffer sb = new StringBuffer();
            sb.append(PREFIX);
            int code = (c >> 8);
            String tmp = Integer.toHexString(code);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
            code = (c & 0xFF);
            tmp = Integer.toHexString(code);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
            return sb.toString();
        } else {
            return Character.toString(c);
        }
    }

    /**
     * Ascii to native string. It's same as execut native2ascii.exe -reverse.
     * 
     * @param str ascii string
     * @return native string
     */
    public static String ascii2Native(String str) {
        StringBuffer sb = new StringBuffer();
        int begin = 0;
        int index = str.indexOf(PREFIX);
        while (index != -1) {
            sb.append(str.substring(begin, index));
            sb.append(ascii2Char(str.substring(index, index + 6)));
            begin = index + 6;
            index = str.indexOf(PREFIX, begin);
        }
        sb.append(str.substring(begin));
        return sb.toString();
    }

    /**
     * Ascii to native character.
     * 
     * @param str ascii string
     * @return native character
     */
    private static char ascii2Char(String str) {
        if (str.length() != 6) {
            throw new IllegalArgumentException("Ascii string of a native character must be 6 character.");
        }
        if (!PREFIX.equals(str.substring(0, 2))) {
            throw new IllegalArgumentException("Ascii string of a native character must start with \"\\u\".");
        }
        String tmp = str.substring(2, 4);
        int code = Integer.parseInt(tmp, 16) << 8;
        tmp = str.substring(4, 6);
        code += Integer.parseInt(tmp, 16);
        return (char) code;
    }

    public static void main(String[] args) {
        String ascii = "\u5fae\u7b11\uff0c\u662f\u4eba\u7c7b\u4f20\u8fbe\u4eb2\u548c\u6001\u5ea6\u7684\u8868\u60c5\u3002\u5728\u5fc3\u60c5\u597d\u7684\u65f6\u5019\uff0c\u4eba\u4eec\u624d\u4f1a\u7ecf\u5e38\u5fae\u7b11\uff1b\u53ea\u6709\u4f1a\u5fae\u7b11\u7684\u4eba\uff0c\u624d\u80fd\u5728\u4eba\u9645\u4ea4\u5f80\u4e2d\u66f4\u53d7\u6b22\u8fce\u3002\u5efa\u8bae:\u6bcf\u5929\u591a\u5fae\u7b11\u51e0\u6b21\uff0c\u4e0d\u4ec5\u662f\u5bf9\u719f\u6089\u7684\u4eba\uff0c\u4e5f\u53ef\u4ee5\u5bf9\u964c\u751f\u4eba\u3002\u8fd9\u6837\uff0c\u4f60\u5c31\u5fc5\u7136\u7ecf\u5e38\u6536\u83b7\u4eba\u4eec\u53cb\u597d\u7684\u7b11\u5bb9\u3002\u5f53\u4f60\u5bf9\u522b\u4eba\u5fae\u7b11\u65f6\uff0c\u4eba\u4eec\u4e5f\u5bf9\u4f60\u5fae\u7b11\uff1b\u4f60\u5fae\u7b11\uff0c\u4e16\u754c\u4e5f\u5fae\u7b11\u3002";
        System.out.println(ascii2Native(ascii));
    }
}
