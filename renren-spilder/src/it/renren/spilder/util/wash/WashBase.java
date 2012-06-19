package it.renren.spilder.util.wash;

import it.renren.spilder.main.Constants;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public abstract class WashBase {

    protected static final ConfigurableApplicationContext ctx = new FileSystemXmlApplicationContext(
                                                                                                    new String[] { Constants.SPRING_CONFIG_FILE });

    public static void initArgs(String[] args) {
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                String[] keyValue = arg.split("=");
                String value = "";
                if (keyValue.length == 1) {
                    value = "";
                } else {
                    value = keyValue[1];
                }
                System.setProperty(keyValue[0], value);
            }
        }
    }

    public static void release() {
        ctx.close();
    }
}
