package it.renren.spilder.test;

public class ObjectTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ObjectTest test = new ObjectTest();
		T t = test.new T();
		t.setStr("Test String");
		String str = t.getStr();
		str = "Other Test String";
		System.out.println(t.getStr());
		System.out.println(str);
	}
	class T{
		String str;

		public String getStr() {
			return str;
		}

		public void setStr(String str) {
			this.str = str;
		}
	}
}
