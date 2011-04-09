package it.renren.spilder.util.log;

/**
 * @author:fenglb@sunline.cn
 * @version: 1.0.0 2010-5-5 下午08:37:48 类说明:
 */
import java.util.*;
import java.io.*;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

public class PropertyManager {

    private static Logger        log              = Logger.getLogger(PropertyManager.class.getName());

    /**
     * The Major version number of cnjsp.net i.e 1.x
     */
    private static final int     MAJOR_VERSION    = 1;

    /**
     * The Minor version number. i.e. x.1.x.
     */
    private static final int     MINOR_VERSION    = 2;

    /**
     * The revision version number . i.e. x.x.1.
     */
    private static final int     REVISION_VERSION = 5;

    private static Object        managerLock      = new Object();

    private String               propsFName       = "bimis.properties";

    private String               propsPName       = "/";

    private static java.util.Map propertyManagers;

    public static PropertyManager getInstance(String propertyFileName) {
        synchronized (managerLock) {
            if (null == propertyManagers) {
                propertyManagers = new Hashtable();
            }
            if (propertyManagers.containsKey(propertyFileName)) {
                return (PropertyManager) propertyManagers.get(propertyFileName);
            } else {
                PropertyManager pm = new PropertyManager(propertyFileName);
                if (null == pm) {
                    return null;
                }
                propertyManagers.put(propertyFileName, pm);
                return pm;
            }
        }
    }

    /**
     * Returns the version number as a String. i.e. -- major.minor.revision
     */
    public static String getVersion() {
        return MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION;
    }

    /**
     * Returns the major version number . i.e. -- 1.x.x
     */
    public static int getVersionMajor() {
        return MAJOR_VERSION;
    }

    /**
     * Returns the minor version number . i.e. -- x.1.x
     */
    public static int getVersionMinor() {
        return MINOR_VERSION;
    }

    /**
     * Returns the revision version number. i.e. -- x.x.1
     */
    public static int getVersionRevision() {
        return REVISION_VERSION;
    }

    private Properties G_properties   = null;

    private Object     propertiesLock = new Object();

    // private String resourceURI;
    private String     path           = null;

    /**
     * Creates a new PropertyManager. Singleton access only.
     */
    private PropertyManager(String propertyFileName){
        BasicConfigurator.configure();
        // this.resourceURI = resourceURI;
        try {
            propsPName = new File(propertyFileName).getParent();
            propsFName = propertyFileName;
            File pf = new File(propsPName);
            if (!pf.exists()) {
                pf.mkdirs();
            }
            this.path = new File(propertyFileName).getAbsolutePath();
        } catch (Exception e) {
            log.error(e);
            this.path = propsFName;
        }

        // Need an additional check
        if (G_properties == null) {
            synchronized (propertiesLock) {
                loadProps();
            }
        }
    }

    /**
     * getProperty
     * 
     * @param name -the name of the property to get.
     * @return the property specified by name.
     */
    public String getProperty(String name) {
        return getProperty(name, null);
    }

    /**
     * @param name the name of the property to get.
     * @return the property specified by name.
     */
    public String getProperty(String name, String defaultString) {
        // If properties aren't loaded yet. We also need to make this thread
        // safe, so synchronize...
        if (G_properties == null) {
            synchronized (propertiesLock) {
                // Need an additional check
                if (G_properties == null) {
                    if (loadProps() == false) {
                        return null;
                    }
                }
            }
        }
        String property = G_properties.getProperty(name);
        if (property == null) {
            return defaultString;
        } else {
            return property.trim();
        }
    }

    /**
     * Sets a Jive property. Because the properties must be saved to disk every time a property is set, property setting
     * is relatively slow.
     */
    public void setProperty(String name, String value) {
        // Only one thread should be writing to the file system at once.
        synchronized (propertiesLock) {
            // Create the properties object if necessary.
            if (G_properties == null) {
                loadProps();
            }
            G_properties.setProperty(name, value);
            // 属性全部加完后用显式调用saveProperty()方法来保存
            // saveProps();
        }
    }

    public void deleteProperty(String name) {
        // Only one thread should be writing to the file system at once.
        synchronized (propertiesLock) {
            // Create the properties object if necessary.
            if (G_properties == null) {
                loadProps();
            }
            G_properties.remove(name);
            // 属性全部删完后用显式调用saveProperty()方法来保存
            // saveProps();
        }
    }

    public Enumeration PropertyNames() {
        // If properties aren't loaded yet. We also need to make this thread
        // safe, so synchronize...
        if (G_properties == null) {
            synchronized (propertiesLock) {
                // Need an additional check
                if (G_properties == null) {
                    loadProps();
                }
            }
        }
        return G_properties.propertyNames();
    }

    /**
     * Loads properties from the disk --- pc-mcare.properties.
     */
    private boolean loadProps() {
        boolean m_success = true;
        // 2001-08-25 reload()
        if (G_properties != null) {
            G_properties.clear();
        } else {
            G_properties = new Properties();
        }
        // log.info("Load properties from: " + this.path);
        InputStream in = null;
        try {
            // in = getClass().getResourceAsStream(resourceURI);
            /*
             * G_properties.load(in); path = G_properties.getProperty("path");
             */
            in = new FileInputStream(this.path);
            G_properties.load(in);
        } catch (Exception e) {
            log.error("Error reading properties in PropertyManager.loadProps()\n" + e);
            m_success = false;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
        }
        return m_success;
    }

    /**
     * Saves properties to disk.
     */
    public void saveProperty() {
        // path = G_properties.getProperty("path").trim();
        log.info("Save properties to: " + this.path);
        OutputStream out = null;
        try {
            File file = new File(this.path);
            if (!file.isFile()) {
                file.createNewFile();
                log.info("Creating properties file: " + this.path);
                // this.path = getClass().getResource(propsPName +
                // propsFName).getPath();
            }
            out = new FileOutputStream(this.path);
            G_properties.store(out, this.path);
        } catch (Exception ioe) {
            log.error("There was an error writing properties to " + path + ". "
                      + "Ensure that the path exists and that the cnjbb process has permission " + "to write to it -- "
                      + ioe);
            // iolog4j.logError(e);
        } finally {
            try {
                out.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Returns true if the properties are readable. This method is mainly valuable at setup time to ensure that the
     * properties file is setup correctly.
     */
    public boolean propFileIsReadable() {
        try {
            // InputStream in = getClass().getResourceAsStream(resourceURI);
            File file = new File(this.path);
            return file.canRead();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true if the jive.properties file exists where the path property purports that it does.
     */
    public boolean propFileExists() {
        /*
         * String path = getProp("path", null); if (path == null) { return false; }
         */
        File file = new File(this.path);
        if (file.isFile()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if the properties are writable. This method is mainly valuable at setup time to ensure that the
     * properties file is setup correctly.
     */
    public boolean propFileIsWritable() {
        // String path = getProp("path", null);
        File file = new File(this.path);
        if (file.isFile()) {
            // See if we can write to the file
            if (file.canWrite()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
