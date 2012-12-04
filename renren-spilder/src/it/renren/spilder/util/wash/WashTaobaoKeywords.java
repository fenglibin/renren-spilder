package it.renren.spilder.util.wash;

import it.renren.spilder.util.FileUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WashTaobaoKeywords {

    public static void main(String[] args) throws IOException {
        String keyFile = "d:/taobao_keywords.txt";
        keyFile = "d:/hotkey.txt";
        String[] keys = FileUtil.read(keyFile).split(",");
        Map<String, String> keyMap = new HashMap<String, String>();
        for (String key : keys) {
            keyMap.put(key, key);
        }
        Set<Map.Entry<String, String>> mapSet = keyMap.entrySet();
        for (Map.Entry<String, String> entry : mapSet) {
            System.out.print(entry.getKey() + ",");
        }
    }
}
