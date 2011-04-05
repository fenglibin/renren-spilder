package it.renren.spilder.util.log;
/**
 * @author:fenglb@sunline.cn
 * @version: 1.0.0 2010-5-6 上午11:58:45
 * 类说明:  
 */
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.DefaultRepositorySelector;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RepositorySelector;
import org.apache.log4j.spi.RootCategory;

public class LogManager
{
    /**
     * @deprecated
     */
    public static final String DEFAULT_CONFIGURATION_FILE = "log4j.properties";
    static final String DEFAULT_XML_CONFIGURATION_FILE = "log4j.xml";
    /**
     * @deprecated
     */
    public static final String DEFAULT_CONFIGURATION_KEY
	= "log4j.configuration";
    /**
     * @deprecated
     */
    public static final String CONFIGURATOR_CLASS_KEY
	= "log4j.configuratorClass";
    /**
     * @deprecated
     */
    public static final String DEFAULT_INIT_OVERRIDE_KEY
	= "log4j.defaultInitOverride";
    private static Object guard = null;
    private static RepositorySelector repositorySelector;
    
    public static void setRepositorySelector
	(RepositorySelector selector, Object guard)
	throws IllegalArgumentException {
	if (LogManager.guard != null && LogManager.guard != guard)
	    throw new IllegalArgumentException
		      ("Attempted to reset the LoggerFactory without possessing the guard.");
	if (selector == null)
	    throw new IllegalArgumentException
		      ("RepositorySelector must be non-null.");
	LogManager.guard = guard;
	repositorySelector = selector;
    }
    
    public static LoggerRepository getLoggerRepository() {
	return repositorySelector.getLoggerRepository();
    }
    
    public static Logger getRootLogger() {
	return repositorySelector.getLoggerRepository().getRootLogger();
    }
    
    public static Logger getLogger(String name) {
	return repositorySelector.getLoggerRepository().getLogger(name);
    }
    
    public static Logger getLogger(Class clazz) {
	return repositorySelector.getLoggerRepository()
		   .getLogger(clazz.getName());
    }
    
    public static Logger getLogger(String name, LoggerFactory factory) {
	return repositorySelector.getLoggerRepository().getLogger(name,
								  factory);
    }
    
    public static Logger exists(String name) {
	return repositorySelector.getLoggerRepository().exists(name);
    }
    
    public static Enumeration getCurrentLoggers() {
	return repositorySelector.getLoggerRepository().getCurrentLoggers();
    }
    
    public static void shutdown() {
	repositorySelector.getLoggerRepository().shutdown();
    }
    
    public static void resetConfiguration() {
	repositorySelector.getLoggerRepository().resetConfiguration();
    }
    
    static {
	Hierarchy h = new Hierarchy(new RootCategory(Level.DEBUG));
	repositorySelector = new DefaultRepositorySelector(h);
	String override
	    = OptionConverter.getSystemProperty("log4j.defaultInitOverride",
						null);
	if (override == null || "false".equalsIgnoreCase(override)) {
	    String configurationOptionStr
		= OptionConverter.getSystemProperty("log4j.configuration",
						    null);
	    String configuratorClassName
		= OptionConverter.getSystemProperty("log4j.configuratorClass",
						    null);
	    URL url = null;
	    if (configurationOptionStr == null) {
		url = Loader.getResource("log4j.xml");
		if (url == null)
		    url = Loader.getResource("log4j.properties");
	    } else {
		try {
		    url = new URL(configurationOptionStr);
		} catch (MalformedURLException ex) {
		    url = Loader.getResource(configurationOptionStr);
		}
	    }
	    if (url != null) {
		LogLog.debug("Using URL [" + url
			     + "] for automatic log4j configuration.");
		OptionConverter.selectAndConfigure(url, configuratorClassName,
						   getLoggerRepository());
	    } else
		LogLog.debug("Could not find resource: ["
			     + configurationOptionStr + "].");
	}
    }
}


