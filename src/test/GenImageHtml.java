package test;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GenImageHtml {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String imagesDir = "H:/images/1";
        File imageDirFile = new File(imagesDir);
        File[] imageDirs = imageDirFile.listFiles();
        Map<String, String> map = new HashMap<String, String>();
        for (File dir : imageDirs) {
            if (dir.isDirectory() && dir.listFiles().length > 0) {
                File oneFile = dir.listFiles()[0];
                String temp = dir.getName() + "/" + oneFile.getName();
                map.put(dir.getName(), temp);
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
            if (i % 2 != 0) {
                oneStringImage += "<tr>";
                oneStringWords += "<tr>";
            }
            oneStringImage += "<td><img src='" + value + "' border='0' width='0' height='0' onload='AutoResizeImage(100,0,this)'></td>";
            oneStringWords += "<td>" + key + "</td>";
            if (i % 2 == 0) {
                oneStringImage += "</tr>";
                oneStringWords += "</tr>";
                allString += oneStringImage;
                allString += oneStringWords;
                oneStringImage = "";
                oneStringWords = "";
            }
            i++;

        }
        if (i % 2 == 0) {
            oneStringImage += "<td></td>";
            oneStringWords += "<td></td>";
            allString += oneStringImage;
            allString += oneStringWords;
            oneStringImage = "";
            oneStringWords = "";
        }
        System.out.println(allString);
    }
}
