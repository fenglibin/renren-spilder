package it.renren.spilder.util.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PrintFileNamesFromDirectory {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("d:/test/url.txt"));
        String line = br.readLine();
        while (line != null) {
            line = br.readLine();
            if (line != null) {
                String name = new File(line).getName();
                System.out.println(name);
            }
        }
        br.close();
    }

}
