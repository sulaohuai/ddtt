package com.ddtt.testdata.util;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	public static boolean isEmpty(String s) {
		if (s == null) {
			return true;
		}

		return "".equals(s.trim());
	}
	
	public static boolean isEmpty(Object s) {
		if (s == null) {
			return true;
		}
		
		if(s instanceof String){
			return "".equals((String)s);
		}

		throw new IllegalArgumentException(s.toString() + " is not a String object.");
	}

	public static String[] split(String str, String delimiter) {
		if ((str == null) || ("".equals(str.trim()))) {
			return new String[0];
		}

		Vector<String> result = new Vector<String>(3, 3);

		int index = 0;
		int pos = 0;
		int count = 0;
		while (true) {
			pos = str.indexOf(delimiter, index);

			if (pos == -1) {
				result.add(count, str.substring(index));
				++count;
				break;
			}

			result.add(count, str.substring(index, pos));

			++count;

			index = pos + delimiter.length();
		}

		String[] tmp = new String[count];
		System.arraycopy(result.toArray(), 0, tmp, 0, count);

		result = null;
		return tmp;
	}

	public static String[][] splitToTwoDimensionalArray(String s, String delim1, String delim2) {
		s = replaceAll(s, "[", "");
		s = replaceAll(s, "]", "");
		s = replaceAll(s, " ", "");
		String[] r1 = split(s, delim1);
		String[][] result = new String[r1.length][];
		for (int i = 0; i < r1.length; ++i) {
			result[i] = split(r1[i], delim2);
		}
		return result;
	}

	public static String replaceAll(String str, String key, String replacement) {
		StringBuffer sb = new StringBuffer();
		if (isNotEmpty(str)) {
			String[] parts = split(str, key);
			sb.append(parts[0]);
			for (int i = 1; i < parts.length; ++i) {
				sb.append(replacement + parts[i]);
			}
		}
		return sb.toString();
	}

	public static boolean isNotEmpty(String str) {
		return (!(isEmpty(str)));
	}

	public static boolean isMatch(String format, String value) {
		if (format == null) {
			return true;
		}
		Pattern p = Pattern.compile(format);
		Matcher m = p.matcher(value);
		return m.matches();
	}
}
