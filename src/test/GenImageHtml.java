package test;

import it.renren.spilder.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GenImageHtml {

    private static String imagesDir = "H:/images/1";

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        step3();
    }

    /**
     * 在每个文件夹下面创建一个表示当前文件夹名称的文件:who.txt。此方法只能够是第一次执行
     * 
     * @throws Exception
     */
    public static void step1() throws Exception {
        File imageDirFile = new File(imagesDir);
        File[] imageDirs = imageDirFile.listFiles();
        for (File dir : imageDirs) {
            if (dir.isDirectory() && dir.listFiles().length > 0) {
                String who = "who.txt";
                File fileWho = new File(dir.getAbsolutePath() + File.separator + who);
                if (!fileWho.exists()) {
                    FileUtil.writeFile(dir.getAbsolutePath() + File.separator + who, dir.getName());
                }
            }
        }
    }

    /**
     * 将所有的文件夹重命名，以便于andorid识别，因为androiv不识别中文文件名
     */
    public static void step2() {
        File imageDirFile = new File(imagesDir);
        File[] imageDirs = imageDirFile.listFiles();
        for (File dir : imageDirs) {
            if (dir.isDirectory() && dir.listFiles().length > 0) {
                File dest = new File(imagesDir + "/" + String.valueOf(System.currentTimeMillis()));
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

    public static void step3() throws IOException {
        File imageDirFile = new File(imagesDir);
        File[] imageDirs = imageDirFile.listFiles();
        Map<String, String> map = new HashMap<String, String>();
        Map<String, String> mapWho = new HashMap<String, String>();
        int oneRowImages = 2;
        for (File dir : imageDirs) {
            if (dir.isDirectory() && dir.listFiles().length > 0) {
                File[] subListFiles = dir.listFiles();
                for (File oneFile : subListFiles) {
                    if (FileUtil.isImageUsualFile(oneFile.getName())) {// 取第一个图片文件
                        String temp = dir.getName() + "/" + oneFile.getName();
                        map.put(dir.getName(), temp);
                        break;
                    }
                }
                String whoName = FileUtil.getFileContent(dir.getAbsolutePath() + "/who.txt");
                mapWho.put(dir.getName(), whoName);
            }            
        }
        Iterator<String> it = map.keySet().iterator();
        int i = 1;
        String allString = "";
        String oneStringImage = "";
        String oneStringWords = "";
        while (it.hasNext()) {
            String key = it.next();
            String value = map.get(key);
            if (i % oneRowImages != 0) {
                oneStringImage += "<tr>";
                oneStringWords += "<tr>";
            }
            oneStringImage += "<td><a href='" + key + "/index.html'><img src='" + value
                              + "' border='0' width='0' height='0' onload='AutoResizeImage(100,0,this)'></a></td>";
            oneStringWords += "<td>" + mapWho.get(key) + "</td>";
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
        }else if(i % oneRowImages == 2){
            oneStringImage += "<td></td><td></td></tr>";
            oneStringWords += "<td></td><td></td></tr>";
            allString += oneStringImage;
            allString += oneStringWords;
            oneStringImage = "";
            oneStringWords = "";
        }
        System.out.println(allString);
    }

    public static void step4() throws Exception {
        File imageDirFile = new File(imagesDir);
        File[] imageDirs = imageDirFile.listFiles();
        String index_mode = FileUtil.getFileContent(imagesDir + "/" + "index_model.htm");
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
                imageNameStrngs = imageNameStrngs.substring(0, imageNameStrngs.length() - 1);
                String index_mode_this = index_mode.replace("#imageNameStrngs#", imageNameStrngs);
                FileUtil.writeFile(dir.getAbsolutePath() + File.separator + "index.htm", index_mode_this);
            }
        }
    }
}
