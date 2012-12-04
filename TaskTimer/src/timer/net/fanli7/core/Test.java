package net.fanli7.core;

import java.util.List;

import open4j.OpenYiqifa;
import open4j.OpenYiqifaException;
import open4j.data.GwkTuanWebSite;

public class Test {

    private static String customKey     = "13527309584076703";
    private static String customSecrect = "d4be037a5d8a37daf77d353bada3625a";

    /**
     * @param args
     * @throws OpenYiqifaException
     */
    public static void main(String[] args) throws OpenYiqifaException {
        // TODO Auto-generated method stub
        OpenYiqifa open = new OpenYiqifa();
        open.setOAuthConsumer(customKey, customSecrect);
        List<GwkTuanWebSite> list = open.getGwkTuanWibsetList();

        for (GwkTuanWebSite cat : list) {
            System.out.println(cat.getName());
        }
    }

}
