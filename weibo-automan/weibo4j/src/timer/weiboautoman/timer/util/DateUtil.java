package weiboautoman.timer.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 类DateUtil.java的实现描述：日期工具类
 * 
 * @author fenglibin 2011-11-27 下午02:36:43
 */
public class DateUtil {

    /* 默认的日期格式 */
    private static final String DEFAULT_DATE_FORMAT      = "yyyy/MM/dd";
    /* 默认的日期时间格式 */
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 根据传入的日期格式或者是默认的日期格式，返回当前的系统日期，格式可以为： String format = "yyyy-MM-dd HH:mm:ss,SSS";等
     * 
     * @param format
     * @return 根据传入的格式返回日期
     */
    public static String getNow(String format) {
        if (null == format || "".equals(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String date = sdf.format(new Date());
        return date;
    }

    /**
     * 将timestamp时间戳转换为日期时间格式，以字符串格式返回
     * 
     * @param timestamp
     * @return
     */
    public static String timeStamp2Date(long timestamp) {
        timestamp = timestamp * 1000;
        String date = new java.text.SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT).format(new java.util.Date(timestamp));
        return date;
    }

    /**
     * 将日期字符串转换为timestamp时间戳，传入的字符串的日期格式要求为：yyyy-MM-dd HH:mm:ss
     * 
     * @param dateString
     * @return
     */
    public static Long date2TimeStamp(String dateString) {
        Date date = parseDate(dateString, DEFAULT_DATE_TIME_FORMAT);
        return date2TimeStamp(date);
    }

    /**
     * 将日期转换为timestamp时间戳
     * 
     * @param dateString
     * @return
     */
    public static Long date2TimeStamp(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
        String time = df.format(date);
        Timestamp ts = Timestamp.valueOf(time);
        return ts.getTime() / 1000;
    }

    /**
     * 给指定的日期增加给定的天数
     * 
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        return addHours(date, days * 24);
    }

    /**
     * 给指定的日期增加给定的小时数
     * 
     * @param date
     * @param hours
     * @return
     */
    public static Date addHours(Date date, int hours) {
        return addMinutes(date, hours * 60);
    }

    /**
     * 给指定的日期增加给定的分钟数
     * 
     * @param date
     * @param minutes
     * @return
     */
    public static Date addMinutes(Date date, int minutes) {
        return addSeconds(date, minutes * 60);
    }

    /**
     * 在给定的日期上增加相应的秒数
     * 
     * @param date
     * @param seconds
     * @return
     */
    public static Date addSeconds(Date date, int seconds) {
        Calendar c = Calendar.getInstance();

        c.setTime(date);
        c.add(Calendar.SECOND, seconds);

        return c.getTime();
    }

    /**
     * 将给定的字符串解析为给定格式的日期格式
     * 
     * @param date
     * @param dateformat
     * @return
     */
    public static Date parseDate(String date, String dateformat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateformat);

        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取给定的两个日期之间相差的分钟数
     * 
     * @param firstDate
     * @param lastDate
     * @return
     */
    public static int getTimeIntervalMins(Date firstDate, Date lastDate) {
        long intervals = lastDate.getTime() - firstDate.getTime() + (60 * 1000);

        if (intervals > 0) {
            long longMins = (intervals / (60 * 1000)) % 60;

            return new Long(longMins).intValue();
        }

        return 0;
    }

    /**
     * 获取两个结定的日期之间相差的小时数
     * 
     * @param firstDate
     * @param lastDate
     * @return
     */
    public static int getTimeIntervalHours(Date firstDate, Date lastDate) {
        long intervals = lastDate.getTime() - firstDate.getTime() + (60 * 1000);

        if (intervals > 0) {
            long longHours = (intervals / (60 * 60 * 1000)) % 24;

            return new Long(longHours).intValue();
        }

        return 0;
    }

    /**
     * 获取两个日期之间相差的天数
     * 
     * @param firstDate
     * @param lastDate
     * @return
     */
    public static int getTimeIntervalDays(Date firstDate, Date lastDate) {
        long intervals = lastDate.getTime() - firstDate.getTime() + (60 * 1000);

        if (intervals > 0) {
            long daysd = intervals / (24 * 60 * 60 * 1000);

            return new Long(daysd).intValue();
        }

        return 0;
    }

    /**
     * 将给定的日期转换为默认的字符串格式：yyyy/MM/dd/输出
     * 
     * @param date
     * @return
     */
    public static String toLocaleString(Date date) {
        return toLocaleString(date, null);
    }

    /**
     * 将结定的日期转换为给定的日期格式字符串并输出
     * 
     * @param date
     * @param dateFormat
     * @return
     */
    public static String toLocaleString(Date date, String dateFormat) {
        if (date == null) {
            return "";
        }

        if (StringUtil.isNull(dateFormat)) {
            return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(date);
        }

        return new SimpleDateFormat(dateFormat).format(date);
    }

    public static void main(String[] args) {
        System.out.println(timeStamp2Date(1322375412));
        System.out.println(date2TimeStamp(new Date()));
    }
}
