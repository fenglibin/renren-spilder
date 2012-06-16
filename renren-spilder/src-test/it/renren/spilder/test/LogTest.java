package it.renren.spilder.test;



import it.renren.spilder.util.log.Log4j;

public class LogTest {
	private static Log4j log4j = new Log4j(LogTest.class.getName());
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		log4j.logDebug("test log");
		log4j.logError("test log");
	}

}
