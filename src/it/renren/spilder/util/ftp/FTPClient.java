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
 * FTP�ͻ��ˣ������ϴ��ļ���FTP
 * 
 * @author fenglb@sunline.cn
 */
public class FTPClient {

    private static Log4j  log4j           = new Log4j(FTPClient.class);
    private FtpClient     ftpClient;
    private String        host;                                        // FTP������ַ
    private int           port;                                        // �˿ں�
    private String        username;                                    // ��½�û�
    private String        password;                                    // ��½����
    private int           default_ftpPort = 21;                        // Ĭ��FTP����˿�
    private static String localDirectory  = null;                      // �յ��ĺ�ͬ����ڱ��ص�Ŀ¼

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
     * ��ʹ����ǰ�ļ��������Ŀ¼
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
     * ��FTP��������
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
            ftpClient.binary(); // �����ƴ�������
            return true;
        } catch (Exception e) {
            log4j.logError("ftp server: open failed", e);
            ftpClient = null;
            return false;
        }

    }

    /**
     * ת����FTPָ��Ŀ¼
     * 
     * @param dir FTPĿ¼
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
     * �ϴ��ļ���FTP�������ĸ�Ŀ¼�£����������뱾���ļ���ͬ
     * 
     * @param localFile �����ļ�Ŀ¼���ļ���
     * @throws Exception
     */
    public boolean put(String localFile) throws Exception {
        String fileName = new File(localFile).getName();
        return put(localFile, fileName, null);
    }

    /**
     * �ϴ��ļ���FTP�������ĸ�Ŀ¼��
     * 
     * @param localFile �����ļ�Ŀ¼���ļ���
     * @param fileName �ϴ�����ļ���
     * @throws Exception
     */
    public boolean put(String localFile, String fileName) throws Exception {
        return put(localFile, fileName, null);
    }

    /**
     * �ϴ��ļ���FTP������
     * 
     * @param localPathAndFileName �����ļ�Ŀ¼���ļ���
     * @param fileName �ϴ�����ļ��������Ϊnull�����ϴ��ļ����뱾���ļ�����ͬ
     * @param ftpDirectory FTPĿ¼��:/path1/pathb2/,���Ŀ¼�����ڷ��ش���
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
     * ��FTP�������������ļ������������ļ�����
     * 
     * @param ftpFile FTP��������·�����ļ���
     * @param localFile ����·�����ļ���
     * @return
     * @throws Exception
     */
    public long get(String ftpFile) throws Exception {
        String localFile = getFtpFileName(ftpFile);
        return get(ftpFile, localFile);
    }

    /**
     * ��FTP�������������ļ������������ļ�����
     * 
     * @param ftpFile FTP��������·�����ļ���
     * @param localFile ����·�����ļ���
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
     * ����FTPĿ¼�µ��ļ��б�
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
     * ɾ��FTP�ļ�
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
     * ɾ��FTPĿ¼
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
     * �ر�FTP����
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
