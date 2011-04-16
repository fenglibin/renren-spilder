package it.renren.spilder.util.wash;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import it.renren.spilder.main.Constants;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class RemoveUnusedImages {

	private static final ConfigurableApplicationContext ctx = new FileSystemXmlApplicationContext(
			new String[] { Constants.SPRING_CONFIG_FILE });

	static Statement st = null;

	static Statement stFan = null;
	static {
		try {
			st = ((DataSource) ctx.getBean("dataSource")).getConnection()
					.createStatement();
			stFan = ((DataSource) ctx.getBean("dataSourceFanti"))
					.getConnection().createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException {
		String imageDir = "/home/fenglibin/www/www.renren.it/uploads/allimg";
		checkImages(imageDir);

	}

	public static void checkImages(String imagesDir) throws SQLException {
		File file = new File(imagesDir);
		File[] files = file.listFiles();
		for (File thisFile : files) {
			if (thisFile.isDirectory()) {
				checkImages(thisFile.getAbsolutePath());
			}
			String name = thisFile.getName();
			String namesmall = name.toLowerCase();
			// ֻ���ͼƬ
			if (!(namesmall.endsWith("jpg") || namesmall.endsWith("png")
					|| namesmall.endsWith("bmp") || namesmall.endsWith("gif"))) {
				return;
			}
			ResultSet rs = st
					.executeQuery("select aid from renrenaddonarticle where body like '%"
							+ name + "%'");
			boolean delete = false;
			if (!rs.next()) {
				ResultSet rsFan = stFan
						.executeQuery("select aid from renrenfanti_addonarticle where body like '%"
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
