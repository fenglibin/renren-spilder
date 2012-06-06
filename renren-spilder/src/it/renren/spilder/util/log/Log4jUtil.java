package it.renren.spilder.util.log;

/**
 * @author:fenglb@sunline.cn
 * @version: 1.0.0 2010-5-5 下午08:32:26 类说明:
 */

import it.renren.spilder.main.Constants;

import java.util.Hashtable;
import java.io.File;
import java.util.Enumeration;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class Log4jUtil {

    private static Hashtable instances = null;

    private Logger           logger    = null;

    public static Log4jUtil getInstance(String className) {
        if (instances == null) {
            // setup();
            instances = new Hashtable();
        }
        if (instances.containsKey(className)) {
            return (Log4jUtil) instances.get(className);
        } else {
            Log4jUtil log = new Log4jUtil(className);
            instances.put(className, log);
            return log;
        }
    }

    public static void setup(String logFileName, String logLevel, String datePattern, String appender, String layout,
                             String ConversionPattern) {
        try {
            if (logFileName == null) {
                logFileName = "errMessage.log";
            }
            if (logLevel == null) {
                logLevel = "DEBUG";
            }
            if (datePattern == null) {
                datePattern = "'.'yyyy-MM-dd'.log'";
            }
            if (appender == null) {
                appender = "org.apache.log4j.DailyRollingFileAppender";
            }
            if (layout == null) {
                layout = "org.apache.log4j.PatternLayout";
            }
            if (ConversionPattern == null) {
                ConversionPattern = "[%p] %d{yyyy-MM-dd HH:mm:ss,SSS} [%c] %l Message:%n%m%n";
            }
            String path = Constants.currentPath;
            File pf = null;
            pf = new File(path + "config" + File.separator + "log4j.properties");
            PropertyManager pmLog4j = PropertyManager.getInstance(pf.getAbsolutePath());
            Enumeration props = pmLog4j.PropertyNames();
            while (props != null && props.hasMoreElements()) {
                String propName = (String) props.nextElement();
                pmLog4j.deleteProperty(propName);
            }
            pmLog4j.setProperty("log4j.rootLogger", logLevel + ", bimis");
            pmLog4j.setProperty("log4j.appender.bimis.DatePattern", datePattern);
            pmLog4j.setProperty("log4j.appender.bimis", appender);
            File err = new File(path + "log" + File.separator + "err");
            if (!err.exists()) {/* 这个目录不存在先创建 */
                err.mkdirs();
            }
            pf = new File(path + "log" + File.separator + "err" + File.separator + logFileName);
            pmLog4j.setProperty("log4j.appender.bimis.File", pf.getAbsolutePath());
            pmLog4j.setProperty("log4j.appender.bimis.layout", layout);
            pmLog4j.setProperty("log4j.appender.bimis.layout.ConversionPattern", ConversionPattern);
            pmLog4j.saveProperty();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Log4jUtil(String className){
        logger = LogManager.getLogger(className);
    }

    public void log(String level, Object msg) {
        log(level, msg, null);
    }

    public void log(String level, Object msg, Throwable e) {
        if (logger != null) {
            logger.log((Priority) Level.toLevel(level), msg, e);
        }
    }

    public void log(String level, Throwable e) {
        log(level, null, e);
    }
}
