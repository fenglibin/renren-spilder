package it.renren.timer.job;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunCommand {

    private static Logger log = LoggerFactory.getLogger(RunCommand.class);
    private String        command;

    public void start() {
        try {
            if (log.isInfoEnabled()) {
                log.info("Start Run Command:" + command + ". Now:" + System.currentTimeMillis());
            }
            Runtime.getRuntime().exec(command);
            if (log.isInfoEnabled()) {
                log.info("End Run Command:" + command + ". Now:" + System.currentTimeMillis());
            }
        } catch (IOException e) {
            if (log.isErrorEnabled()) {
                log.error("Run Command Exception:" + e.getMessage(), e);
            }
        }
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
