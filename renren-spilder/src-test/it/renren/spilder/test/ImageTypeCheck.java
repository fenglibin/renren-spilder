package it.renren.spilder.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageTypeCheck {

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws IOException {
        String imagePath = "c:/favicon.png";
        File image = new File(imagePath);
        InputStream is = new FileInputStream(image);
        byte[] bt = new byte[2];
        is.read(bt);
        System.out.println(bytesToHexString(bt));
    }
}
