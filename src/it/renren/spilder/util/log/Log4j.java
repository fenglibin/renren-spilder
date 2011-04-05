package it.renren.spilder.util.log;
/**
 * @author:fenglb@sunline.cn
 * @version: 1.0.0 2010-5-6 上午11:46:14
 * 类说明:  
 */
public class Log4j {
	private Log4jUtil log = null;

	public Log4j(String className) {
		log = Log4jUtil.getInstance(className);
	}

	public Log4j(Class clazz) {
		this(clazz.getName());
	}

	public void logError(String msg) {
		log.log("ERROR", msg);
	}

	public void logError(Throwable e) {
		logError(null, e);
	}

	public void logError(Object msg, Throwable e) {
		log.log("ERROR", msg, e);
	}

	public void logWarn(String msg) {
		log.log("WARN", msg);
	}

	public void logWarn(Throwable e) {
		logWarn(null, e);
	}

	public void logWarn(Object msg, Throwable e) {
		log.log("WARN", msg, e);
	}

	public void logInfo(String msg) {
		log.log("INFO", msg);
	}

	public void logInfo(Throwable e) {
		logInfo(null, e);
	}

	public void logInfo(Object msg, Throwable e) {
		log.log("INFO", msg, e);
	}

	public void logDebug(String msg) {
		try {
			log.log("DEBUG", msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void logDebug(Throwable e) {
		logDebug(null, e);
	}

	public void logDebug(Object msg, Throwable e) {
		try {
			log.log("DEBUG", msg, e);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}


