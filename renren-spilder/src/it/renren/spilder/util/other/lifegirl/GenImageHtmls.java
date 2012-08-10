package it.renren.spilder.util.other.lifegirl;

import it.renren.spilder.util.FileUtil;
import it.renren.spilder.util.ImageUtil;
import it.renren.spilder.util.NumberUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class GenImageHtmls {

    private static String imagesDir    = "/usr/fenglibin/images/lifegirl_update";
    private static String images_model = "model/girls/images_model.htm";
    private static String index        = "model/girls/index.htm";
    private static String charset      = "gbk";

    public static void main(String[] args) throws Exception {
        step1();
        // step2();
    }

    public static void step1() throws Exception {
        File imageDirFile = new File(imagesDir);
        File[] imageDirs = imageDirFile.listFiles();
        Map<Integer, String> map = new TreeMap<Integer, String>();
        for (File dir : imageDirs) {
            if (dir.isDirectory() && dir.listFiles().length > 0) {
                if (NumberUtil.isNumber(dir.getName())) {
                    map.put(Integer.parseInt(dir.getName()), dir.getAbsolutePath());
                }
            }
        }
        for (Map.Entry<Integer, String> oneMap : map.entrySet()) {
            GenImageHtml genImage = new GenImageHtml();
            genImage.setSeqNumber(String.valueOf(oneMap.getKey()));
            genImage.setImagesDir(oneMap.getValue());
            genImage.genHtml();
        }

    }

    /**
     * 生成首页
     * 
     * @throws Exception
     */
    public static void step2() throws Exception {
        File imageDirFile = new File(imagesDir);
        File[] imageDirs = imageDirFile.listFiles();
        Map<Integer, String> map = new TreeMap<Integer, String>();
        for (File dir : imageDirs) {
            if (dir.isDirectory() && dir.listFiles().length > 0) {
                File subImageDirFile = new File(dir.getAbsolutePath());
                File[] subImageDirs = subImageDirFile.listFiles();
                String thisTypeImage = "";
                boolean findImage = false;
                for (File subDir : subImageDirs) {
                    if (subDir.isDirectory() && subDir.listFiles().length > 0) {
                        File[] subListFiles = subDir.listFiles();
                        for (File oneFile : subListFiles) {
                            if (FileUtil.isImageUsualFileByExt(oneFile.getName())) {// 取第一个图片文件
                                BufferedImage src = ImageUtil.InputImage(oneFile.getAbsolutePath());
                                int height = src.getHeight();
                                if (height >= 556) {
                                    thisTypeImage = oneFile.getAbsolutePath().replace(imagesDir + "/", "");
                                    findImage = true;
                                    break;
                                }

                            }
                        }
                        if (findImage) {
                            break;
                        }
                    }
                }
                map.put(Integer.parseInt(dir.getName()), thisTypeImage);
            }
        }
        int oneRowImages = 2;
        int i = 1;
        String allString = "";
        String oneStringImage = "";
        String oneStringWords = "";
        for (Map.Entry<Integer, String> oneMap : map.entrySet()) {
            int key = oneMap.getKey();
            String value = oneMap.getValue();
            if (i % oneRowImages != 0) {
                oneStringImage += "<tr>";
                oneStringWords += "<tr>";
            }
            oneStringImage += "<td><a href='" + key + "/images.htm' target='blank'><img src='" + value
                              + "' border='0' width='0' height='0' onload='AutoResizeImage(150,0,this)'></a></td>";
            oneStringWords += "<td>《MM欣赏系列" + key + "》<br><div align='center'><a href='soft/lifegirl" + key
                              + ".apk'><font color='red'><b>下载</b></font></a></div></td>";
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
        String images_mode = FileUtil.getFileContent(index, charset);
        images_mode = images_mode.replace("#data#", allString);
        images_mode = images_mode.replace("#seqNumber#", "");
        FileUtil.writeFile(imagesDir + File.separator + "index.htm", images_mode);

        String indexHtml = FileUtil.getFileContent(images_model, charset);
        indexHtml = indexHtml.replace("#seqNumber#", "");
        FileUtil.writeFile(imagesDir + "/" + "images.htm", indexHtml, charset);

    }
}
