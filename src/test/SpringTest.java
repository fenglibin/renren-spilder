package test;

import java.io.InputStream;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SpringTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        final ConfigurableApplicationContext ctx = new FileSystemXmlApplicationContext(new String[] { "beans.xml" });
        System.out.println(ctx);
        String resource="file:/home/fenglibin/proc/renren-spilder/daoconfig/arctiny.xml";
        InputStream in = ClassLoader.getSystemResourceAsStream(resource);
        System.out.println(in);
    }

}
