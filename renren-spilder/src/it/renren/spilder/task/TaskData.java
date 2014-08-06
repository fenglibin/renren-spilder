package it.renren.spilder.task;

import java.util.concurrent.ArrayBlockingQueue;

public class TaskData {

    private static ArrayBlockingQueue<String> urlQueue = new ArrayBlockingQueue<String>(10000);

    public static void putUrl(String url) throws InterruptedException {
        try {
            urlQueue.put(url);
        } catch (InterruptedException e) {

        }
    }

    public static void getUrl() {
        urlQueue.poll();
    }
}
