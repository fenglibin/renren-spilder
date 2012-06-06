package weiboautoman.timer.core;

public class Constants {

    /* 每页默认的处理记录数 */
    public static final int    DEFAULT_PAGE_SIZE        = 100;
    /* 微博成功发送的返回码 */
    public static final int    HTTP_OK_RESPONSE         = 200;
    public static final String SPRING_CONFIG_FILE       = "beans.xml";
    /* 存储的最长错误信息 */
    public static final int    MAX_ERROR_MESSAGE_LENGTH = 2048;
    /* 重复发送记录的错误码 */
    public static final int    REPEAT_MESSAGE_ERR_CODE  = 400;
}
