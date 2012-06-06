package it.renren.spilder.util.wash;

import it.renren.spilder.main.Constants;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class WashBase {

    protected static final ConfigurableApplicationContext ctx = new FileSystemXmlApplicationContext(
                                                                                                    new String[] { Constants.SPRING_CONFIG_FILE });
}
