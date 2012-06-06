package weiboautoman.timer.spring.aop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgAspectService {

    private static Logger logger = LoggerFactory.getLogger(MsgAspectService.class);

    public void doSomethingBefore() {
        logger.warn("Aspect is doing something before.");
    }

    public void doSomethingAfter() {
        logger.warn("Aspect is doing something after.");
    }
}
