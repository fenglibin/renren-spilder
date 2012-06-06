package weiboautoman.timer.job;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类SendMsgToWeibo.java的实现描述：发送立即发送的微博消息
 * 
 * @author fenglibin 2011-10-5 下午05:18:53
 */
public class SendWeiboMsgImmediateJob {

    private static Logger     log = LoggerFactory.getLogger(SendWeiboMsgImmediateJob.class);
    private WeiboSenderThread weiboSenderThread;

    public void setWeiboSenderThread(WeiboSenderThread weiboSenderThread) {
        this.weiboSenderThread = weiboSenderThread;
    }

    protected void run() {
        /** 使用9个线程来发送，每个线程分别执行用户ID以1到9为开始制订的任务 */
        for (int i = 1; i <= 9; i++) {
            WeiboSenderThread senderThread = weiboSenderThread.clone();
            senderThread.setUserIdFirstNumber(i);
            senderThread.setSendType("Y");
            if (log.isDebugEnabled()) {
                log.debug("start thread " + i + " to send weibo message.now is " + new Date());
            }
            new Thread(senderThread).start();
        }
    }

}
