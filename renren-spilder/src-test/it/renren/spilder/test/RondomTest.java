package it.renren.spilder.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.junit.Test;

public class RondomTest extends TestCase {

    @Test
    public void test1() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "1");
        map.put(2, "2");

        int type = -1;
        int rondom = (int) (Math.random() * map.size());
        System.out.println(rondom);
        int index = 0;
        for (Entry<Integer, String> entry : map.entrySet()) {
            if (rondom == index) {
                type = entry.getKey();
            }
            index++;
        }
        System.out.println(type);
    }
}
