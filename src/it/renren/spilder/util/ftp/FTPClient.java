package it.renren.spilder.util.ftp;

import it.renren.spilder.util.log.Log4j;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import sun.net.ftp.*;
import sun.net.TelnetInputStream;
import sun.net.TelnetOutputStream;

/**
 * FTP客户端，可以上传文件到FTP
 * 
 * @author fenglb@sunline.cn
 */
public class FTPClient {

    private static Log4j  log4j           = new Log4j(FTPClient.class);
    private FtpClient     ftpClient;
    private String        host;                                        // FTP主机地址
    private int           port;                                        // 端口号
    private String        username;                                    // 登陆用户
    private String        password;                                    // 登陆密码
    private int           default_ftpPort = 21;                        // 默认FTP服务端口
    private static String localDirectory  = null;                      // 收到的合同存放在本地的目录

    public FTPClient(String host, String username, String password){
        this.host = host;
        this.port = default_ftpPort;
        this.username = username;
        this.password = password;
        initLocalDirectory();
    }

    public FTPClient(String host, int port, String username, String password){
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        initLocalDirectory();
    }

    /**
     * 初使化当前文件所保存的目录
     */
    private static void initLocalDirectory() {
        if (localDirectory == null) {
            localDirectory = System.getProperty("java.io.tmpdir", new File("").getAbsolutePath()) + File.separator
                             + "temp";
        }
        File file = new File(localDirectory);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * 打开FTP服务连接
     * 
     * @return boolean
     * @throws Exception
     */
    public boolean open() {
        if (ftpClient != null && ftpClient.serverIsOpen()) return true;
        try {
            ftpClient = new FtpClient();
            ftpClient.openServer(host, port);
            ftpClient.login(username, password);
            ftpClient.binary(); // 二进制传输数据
            return true;
        } catch (Exception e) {
            log4j.logError("ftp server: open failed", e);
            ftpClient = null;
            return false;
        }

    }

    /**
     * 转换至FTP指定目录
     * 
     * @param dir FTP目录
     * @return boolean
     * @throws Exception
     */
    public boolean cd(String dir) {
        try {
            ftpClient.cd(dir);
            return true;
        } catch (Exception e) {
            log4j.logError("ftp server: cd dir failed", e);
            return false;
        }
    }

    /**
     * 上传文件到FTP服务器的根目录下，并且名称与本地文件相同
     * 
     * @param localFile 本地文件目录和文件名
     * @throws Exception
     */
    public boolean put(String localFile) throws Exception {
        String fileName = new File(localFile).getName();
        return put(localFile, fileName, null);
    }

    /**
     * 上传文件到FTP服务器的根目录下
     * 
     * @param localFile 本地文件目录和文件名
     * @param fileName 上传后的文件名
     * @throws Exception
     */
    public boolean put(String localFile, String fileName) throws Exception {
        return put(localFile, fileName, null);
    }

    /**
     * 上传文件到FTP服务器
     * 
     * @param localPathAndFileName 本地文件目录和文件名
     * @param fileName 上传后的文件名，如果为null，则上传文件名与本地文件名相同
     * @param ftpDirectory FTP目录如:/path1/pathb2/,如果目录不存在返回错误
     * @throws Exception
     */
    public boolean put(String localFile, String fileName, String remotePath) throws Exception {
        TelnetOutputStream os = null;
        FileInputStream is = null;
        if (fileName == null || fileName.equals("")) {
            fileName = new File(localFile).getName();
        }
        try {
            if (!open()) {
                return false;
            }
            if (remotePath != null && remotePath.length() > 0) cd(remotePath);
            os = ftpClient.put(fileName);
            File in = new File(localFile);
            is = new FileInputStream(in);
            byte bytes[] = new byte[1024];
            int readBytes;
            while ((readBytes = is.read(bytes)) != -1) {
                os.write(bytes, 0, readBytes);
            }

        } catch (Exception e) {
            log4j.logError("ftp server: upload file failed", e);
            return false;
        } finally {
            if (is != null) is.close();
            if (os != null) os.close();
        }
        return true;
    }

    /**
     * 从FTP服务器上下载文件并返回下载文件长度
     * 
     * @param ftpFile FTP服务器的路径及文件名
     * @param localFile 本地路径及文件名
     * @return
     * @throws Exception
     */
    public long get(String ftpFile) throws Exception {
        String localFile = getFtpFileName(ftpFile);
        return get(ftpFile, localFile);
    }

    /**
     * 从FTP服务器上下载文件并返回下载文件长度
     * 
     * @param ftpFile FTP服务器的路径及文件名
     * @param localFile 本地路径及文件名
     * @return
     * @throws Exception
     */
    public long get(String ftpFile, String localFile) throws Exception {
        long fileLength = 0;
        TelnetInputStream is = null;
        FileOutputStream os = null;
        try {
            if (!open()) return fileLength;
            is = ftpClient.get(ftpFile);
            File file = new File(localFile);
            log4j.logDebug(file.getAbsolutePath());
            os = new FileOutputStream(file);
            byte bytes[] = new byte[1024];
            int readBytes = 0;
            while ((readBytes = is.read(bytes)) != -1) {
                os.write(bytes, 0, readBytes);
                fileLength += readBytes;
            }
        } catch (Exception e) {
            log4j.logError("ftp server: download file failed", e);
        } finally {
            if (is != null) is.close();
            if (os != null) os.close();
        }
        return fileLength;
    }

    /**
     * 返回FTP目录下的文件列表
     * 
     * @param ftpDirectory
     * @return
     */
    public List list(String ftpDirectory) throws Exception {
        List list = new ArrayList();
        try {
            if (!open()) return list;
            DataInputStream ds = new DataInputStream(ftpClient.nameList(ftpDirectory));
            String fileName = "";
            while ((fileName = ds.readLine()) != null)
                list.add(fileName);
        } catch (Exception e) {
            log4j.logError("ftp server : getFileNameList fail", e);
        }
        return list;
    }

    /**
     * 删除FTP文件
     * 
     * @return boolean
     * @throws Exception
     */
    public boolean del(String ftpDirAndFileName) {
        if (!open()) return false;
        ftpClient.sendServer(" DELE " + ftpDirAndFileName + "\r\n");
        return true;
    }

    /**
     * 删除FTP目录
     * 
     * @return boolean
     * @throws Exception
     */
    public boolean delFTPDirectory(String ftpDirectory) {
        if (!open()) return false;
        ftpClient.sendServer(" XRMD " + ftpDirectory + "\r\n");
        return true;
    }

    /**
     * 关闭FTP连接
     * 
     * @return boolean
     * @throws Exception
     */
    public void close() {
        try {
            if (ftpClient != null && ftpClient.serverIsOpen()) ftpClient.closeServer();
        } catch (Exception e) {
            log4j.logError("ftp server: close failed", e);
        }
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return username;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.username = user;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFtpFileName(String ftpFile) {
        String fileName = "";
        ftpFile = ftpFile.replace("\\", "/");
        String[] str = ftpFile.split("/");
        fileName = str[str.length - 1];
        return fileName;
    }

    public static String getLocalDirectory() {
        initLocalDirectory();
        return localDirectory;
    }
}
