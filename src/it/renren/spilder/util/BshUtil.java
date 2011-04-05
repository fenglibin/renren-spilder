package it.renren.spilder.util;

import bsh.EvalError;
import bsh.Interpreter;

public class BshUtil {
	/**
	 * 根据当前传入的字符串进行计算，并返回结果
	 * @param str
	 * @return
	 * @throws EvalError
	 */
	public static Object eval(String str) throws EvalError{
		Interpreter i=new Interpreter();
		return i.eval(str);
	}
}
