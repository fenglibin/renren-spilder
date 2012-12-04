package it.renren.spilder.util.other;

import it.renren.spilder.util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class T {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        t2();
    }

    private static void t1() throws IOException {
        String path = "E:/work/email/email.txt";
        List<String> listStr = FileUtil.readFile2List(path);
        StringBuilder sb = new StringBuilder("");
        String email = "";
        for (String str : listStr) {
            if (str.indexOf("==") > 0) {
                String[] eamilArr = str.split("==");
                if (eamilArr.length != 2) {
                    continue;
                }
                email = eamilArr[1];
                if (email.indexOf("@") > 0 && email.length() > 10) {
                    sb.append(email).append("\n");
                }
            }
        }
        FileUtil.writeFile("E:/work/email/email_2.txt", sb.toString());
    }

    private static void t2() throws IOException {
        String path = "E:/work/email/xcqc_net_usersemail.txt";
        List<String> listStr = FileUtil.readFile2List(path);
        StringBuilder sb = new StringBuilder("");
        List<String> tempList = new ArrayList<String>();
        for (String str : listStr) {
            str = str.trim();
            if (str.length() < 10) {
                continue;
            }
            if (!tempList.contains(str)) {
                tempList.add(str);
                sb.append(str).append("\n");
            }
        }
        FileUtil.writeFile("E:/work/email/email_ok.txt", sb.toString());
    }
}
