package it.renren.spilder.util;

import bsh.EvalError;
import bsh.Interpreter;

public class BshUtil {
	/**
	 * ���ݵ�ǰ������ַ������м��㣬�����ؽ��
	 * @param str
	 * @return
	 * @throws EvalError
	 */
	public static Object eval(String str) throws EvalError{
		Interpreter i=new Interpreter();
		return i.eval(str);
	}
}
