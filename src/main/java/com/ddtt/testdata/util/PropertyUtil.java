package com.ddtt.testdata.util;

import java.util.Properties;

public class PropertyUtil {
	public static Integer[] getAsIntegerArray(Properties p, String key) {
		String s = StringUtil.replaceAll(p.getProperty(key), "[", "");
		s = StringUtil.replaceAll(s, "]", "");

		String[] ss = StringUtil.split(s, ",");
		Integer[] ii = new Integer[ss.length];

		for (int i = 0; i < ss.length; ++i) {
			ii[i] = new Integer(ss[i]);
		}

		return ii;
	}

	public static String[] getAsStringArray(Properties p, String key) {
		return StringUtil.split(p.getProperty(key), ",");
	}

	public static Integer[][] getAsTwoDimensionalIntegerArray(Properties p, String key) {
		String[][] ss = StringUtil.splitToTwoDimensionalArray(p.getProperty(key), ";", ",");
		Integer[][] ii = new Integer[ss.length][];

		for (int i = 0; i < ss.length; ++i) {
			ii[i] = new Integer[ss[i].length];
			for (int j = 0; j < ss[i].length; ++j) {
				ii[i][j] = new Integer(ss[i][j]);
			}
		}

		return ii;
	}
}
