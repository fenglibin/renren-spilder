package it.renren.spilder.util;

public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String string="ImÖĞ¹úÈË";
        for (int i = 0; i < string.length(); i++) {
            String temp = string.substring(i, i + 1);
            byte[] bt = temp.getBytes();
            char[] array = temp.toCharArray();
            System.out.println(bt.length);
        }
    }

}
