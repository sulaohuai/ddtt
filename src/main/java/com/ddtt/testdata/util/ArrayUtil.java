package com.ddtt.testdata.util;

public class ArrayUtil {
	public static String toString(String[] s) {
		if (s == null)
			return "";

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length; ++i) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(s[i]);
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		String[] s = { "aaa", "bbb", "ccc" };
		System.out.println(toString(s));
	}
}
