package it.renren.spilder.util.wash;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * 删除冗余的图片 类RemoveUnusedImages.java的实现描述：TODO 类实现描述
 * 
 * @author fenglibin 2011-4-29 下午01:00:59
 */
public class RemoveUnusedImages extends WashBase {

    private static Connection connection    = null;
    private static Connection fanConnection = null;

    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        String imageDir = "/home/fenglibin/www/www.renren.it/uploads/allimg";
        connection = ((DataSource) ctx.getBean("dataSource")).getConnection();
        fanConnection = ((DataSource) ctx.getBean("dataSourceFanti")).getConnection();
        checkImages(imageDir);
        connection.close();
        fanConnection.close();

    }

    private static void checkImages(String imagesDir) throws SQLException {
        File file = new File(imagesDir);
        File[] files = file.listFiles();
        for (File thisFile : files) {
            if (thisFile.isDirectory()) {
                checkImages(thisFile.getAbsolutePath());
            } else {
                String name = thisFile.getName();
                String namesmall = name.toLowerCase();
                // 只检测图片
                if (namesmall.endsWith("jpg") || namesmall.endsWith("png") || namesmall.endsWith("bmp")
                    || namesmall.endsWith("gif")) {
                    System.out.println("Starting deal image " + thisFile.getAbsolutePath());
                    ResultSet rs = connection.createStatement().executeQuery(
                                                                             "select aid from renrenaddonarticle where body like '%"
                                                                                     + name + "%'");
                    boolean delete = false;
                    if (!rs.next()) {
                        ResultSet rsFan = fanConnection.createStatement().executeQuery(
                                                                                       "select aid from fanti_addonarticle where body like '%"
                                                                                               + name + "%'");
                        if (!rsFan.next()) {
                            delete = true;
                        }
                    }
                    if (delete) {
                        System.out.println("delete " + thisFile.getAbsolutePath());
                        thisFile.delete();
                    }
                }
            }
        }
    }

}
