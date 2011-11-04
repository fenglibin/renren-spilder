package it.renren.spilder.util.other.lifegirl;

import it.renren.spilder.util.FileUtil;
import it.renren.spilder.util.ImageUtil;
import it.renren.spilder.util.NumberUtil;
import it.renren.spilder.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 类GenImageHtml.java的实现描述：根据指定目录生成图片相关的文件
 * 
 * @author fenglibin 2011-10-28 上午10:30:06
 */
public class GenImageHtml {

    private String imagesDir          = "/usr/fenglibin/images/test";
    /* 每个图片目录图片展示的模板 */
    private String singal_index_model = "model/girls/index_model.htm";
    private String images_model       = "model/girls/images_model.htm";
    private String index              = "model/girls/index.htm";
    private String charset            = "gbk";
    // 当前软件的系列序号
    private String seqNumber          = "";

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        GenImageHtml gen = new GenImageHtml();
        // gen.step1();
        // gen.step2();
        gen.step3();
        gen.step4();
    }

    public void genHtml() throws Exception {
        // step1();
        // step2();
        step3();
        step4();
        step5();
        step6();
    }

    /**
     * 在每个文件夹下面创建一个表示当前文件夹名称的文件:who.txt。此方法只能够是第一次执行
     * 
     * @throws Exception
     */
    public void step1() throws Exception {
        File imageDirFile = new File(imagesDir);
        File[] imageDirs = imageDirFile.listFiles();
        for (File dir : imageDirs) {
            if (dir.isDirectory() && dir.listFiles().length > 0) {
                String who = "who.txt";
                File fileWho = new File(dir.getAbsolutePath() + File.separator + who);
                if (!fileWho.exists()) {
                    String dirName = dir.getName();
                    FileUtil.writeFile(dir.getAbsolutePath() + File.separator + who, dirName, charset);
                }
            }
        }
    }

    /**
     * 将所有的文件夹重命名，以便于andorid识别，因为andorid不识别中文文件名
     */
    public void step2() {
        File imageDirFile = new File(imagesDir);
        File[] imageDirs = imageDirFile.listFiles();
        for (File dir : imageDirs) {
            if (dir.isDirectory() && dir.listFiles().length > 0) {
                if (!NumberUtil.isNumber(dir.getName())) {// 如果目录名已经为数字了，则不进行重命名处理了
                    File dest = new File(imagesDir + File.separator + String.valueOf(System.currentTimeMillis()));
                    dir.renameTo(dest);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 生成图片首页HTML
     * 
     * @throws Exception
     */
    public void step3() throws Exception {
        File imageDirFile = new File(imagesDir);
        File[] imageDirs = imageDirFile.listFiles();
        Map<String, String> map = new HashMap<String, String>();
        Map<String, String> mapWho = new HashMap<String, String>();
        int oneRowImages = 2;
        /* 存放首页图片的文件夹 */
        String index_inco_dir_name = "index_inco";
        String index_inco_dir = imagesDir + File.separator + index_inco_dir_name;
        File file = new File(index_inco_dir);
        if (!file.exists()) {
            file.mkdir();
        }
        for (File dir : imageDirs) {
            if (dir.isDirectory() && dir.listFiles().length > 0 && !dir.getName().equals("res")
                && !dir.getName().equals("index_inco")) {
                File[] subListFiles = dir.listFiles();
                for (File oneFile : subListFiles) {
                    if (FileUtil.isImageUsualFile(oneFile.getName())) {// 取第一个图片文件
                        // String temp = dir.getName() + File.separator + oneFile.getName();
                        FileUtil.copy(oneFile, index_inco_dir + File.separator + oneFile.getName());
                        map.put(dir.getName(), index_inco_dir_name + File.separator + oneFile.getName());
                        break;
                    }
                }
                String whoName = FileUtil.getFileContent(dir.getAbsolutePath() + "/who.txt", charset);
                mapWho.put(dir.getName(), whoName);
            }
        }
        int i = 1;
        String allString = "";
        String oneStringImage = "";
        String oneStringWords = "";
        for (Map.Entry<String, String> oneMap : map.entrySet()) {
            String key = oneMap.getKey();
            String value = oneMap.getValue();
            if (i % oneRowImages != 0) {
                oneStringImage += "<tr>";
                oneStringWords += "<tr>";
            }
            oneStringImage += "<td><a href='" + key + "/index.htm'><img src='" + value
                              + "' border='0' width='0' height='0' onload='AutoResizeImage(150,0,this)'></a></td>";
            String username = mapWho.get(key);
            if (username.length() > 9) {
                username = username.substring(0, 9);
            }
            oneStringWords += "<td>" + username + "</td>";
            if (i % oneRowImages == 0) {
                oneStringImage += "</tr>";
                oneStringWords += "</tr>";
                allString += oneStringImage;
                allString += oneStringWords;
                oneStringImage = "";
                oneStringWords = "";
            }
            i++;

        }
        if (i % oneRowImages == 0) {
            oneStringImage += "<td></td></tr>";
            oneStringWords += "<td></td></tr>";
            allString += oneStringImage;
            allString += oneStringWords;
            oneStringImage = "";
            oneStringWords = "";
        } else if (i % oneRowImages == 2) {
            oneStringImage += "<td></td><td></td></tr>";
            oneStringWords += "<td></td><td></td></tr>";
            allString += oneStringImage;
            allString += oneStringWords;
            oneStringImage = "";
            oneStringWords = "";
        }
        String images_mode = FileUtil.getFileContent(images_model, charset);
        images_mode = images_mode.replace("#data#", allString);
        images_mode = images_mode.replace("#seqNumber#", seqNumber);
        FileUtil.writeFile(imagesDir + File.separator + "images.htm", images_mode);
    }

    /**
     * 生成每个子目录下面的index.htm文件
     * 
     * @throws Exception
     */
    public void step4() throws Exception {
        File imageDirFile = new File(imagesDir);
        File[] imageDirs = imageDirFile.listFiles();
        String signel_index_mode = FileUtil.getFileContent(singal_index_model, charset);
        for (File dir : imageDirs) {
            if (dir.isDirectory() && dir.listFiles().length > 0) {
                File[] subListFiles = dir.listFiles();
                String imageNameStrngs = "";
                for (File oneFile : subListFiles) {
                    if (FileUtil.isImageUsualFile(oneFile.getName())) {// 取第一个图片文件
                        imageNameStrngs += oneFile.getName();
                        imageNameStrngs += ",";
                    }
                }
                if (!StringUtil.isNull(imageNameStrngs)) {
                    imageNameStrngs = imageNameStrngs.substring(0, imageNameStrngs.length() - 1);
                    String index_mode_this = signel_index_mode.replace("#imageNameStrngs#", imageNameStrngs);
                    index_mode_this = index_mode_this.replace("#seqNumber#", seqNumber);
                    FileUtil.writeFile(dir.getAbsolutePath() + File.separator + "index.htm", index_mode_this, charset);
                }
            }
        }
    }

    /**
     * 生成首页
     * 
     * @throws IOException
     */
    public void step5() throws IOException {
        String indexHtml = FileUtil.getFileContent(index, charset);
        indexHtml = indexHtml.replace("#seqNumber#", seqNumber);
        FileUtil.writeFile(imagesDir + File.separator + "index.htm", indexHtml, charset);
    }

    /**
     * 将首页的图片重新生成为大小150px宽度的，减少大小
     * 
     * @throws IOException
     */
    public void step6() throws IOException {
        int maxWidth = 200;
        ImageUtil.changeDirImagesWidthSize(imagesDir + File.separator + "index_inco", null, maxWidth);
    }

    public void setImagesDir(String imagesDir) {
        this.imagesDir = imagesDir;
    }

    public void setSeqNumber(String seqNumber) {
        this.seqNumber = seqNumber;
    }

}
