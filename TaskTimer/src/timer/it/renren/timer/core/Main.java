package it.renren.timer.core;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Main {

    private static Logger                         log = LoggerFactory.getLogger(Main.class);
    private static ConfigurableApplicationContext ctx = null;

    public static void main(String[] args) {
        if (log.isDebugEnabled()) {
            log.debug("begin init spring application context:" + new Date());
        }
        ctx = new FileSystemXmlApplicationContext(new String[] { "beans.xml" });
        if (log.isDebugEnabled()) {
            log.debug("finish init spring application context:" + new Date());
        }
        log.warn("Server Started:" + new Date());
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                ctx.close();
                if (log.isWarnEnabled()) {
                    log.warn("the server is shutdown!");
                }
            }
        });
    }
}
