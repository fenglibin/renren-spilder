package it.renren.spilder.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     * ���ݴ�������ڸ�ʽ������Ĭ�ϵ����ڸ�ʽ�����ص�ǰ��ϵͳ���ڣ���ʽ����Ϊ�� String format = "yyyy-MM-dd HH:mm:ss,SSS";��
     * 
     * @param format
     * @return ���ݴ���ĸ�ʽ��������
     */
    public static String getNow(String format) {
        if (null == format || "".equals(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String date = sdf.format(new Date());
        return date;
    }
}
