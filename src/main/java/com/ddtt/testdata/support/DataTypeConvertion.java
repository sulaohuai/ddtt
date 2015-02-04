package com.ddtt.testdata.support;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.ddtt.testdata.object.Column;
import com.ddtt.testdata.util.DateUtil;
import com.ddtt.testdata.util.StringUtil;

public class DataTypeConvertion {
	private static Logger logger = Logger.getLogger(DataTypeConvertion.class);
	public static final boolean LOB_IS_SUPPORTED = false;

	public static Object typeConvert(Integer sqlType, String value) throws ParseException {
		if (value == null) {
			return null;
		}

		value = value.trim();

		if ((sqlType.intValue() == 1) || (sqlType.intValue() == 12) || (sqlType.intValue() == -1))
			return value;
		if ("".equals(value.trim()))
			return null;
		if ((sqlType.intValue() == 2) || (sqlType.intValue() == 3))
			return new BigDecimal(value);
		if (sqlType.intValue() == -7)
			return Boolean.valueOf(value);
		if (sqlType.intValue() == -6)
			return Byte.valueOf(value);
		if (sqlType.intValue() == 5)
			return Short.valueOf(value);
		if (sqlType.intValue() == 4) {
			return Long.valueOf(value);
		}
		if (sqlType.intValue() == -5)
			return Long.valueOf(value);
		if (sqlType.intValue() == 7)
			return Float.valueOf(value);
		if ((sqlType.intValue() == 6) || (sqlType.intValue() == 8))
			return Double.valueOf(value);
		if ((sqlType.intValue() == -2) || (sqlType.intValue() == -3) || (sqlType.intValue() == -4))
			return new Byte[] { Byte.valueOf(value) };
		if ((sqlType.intValue() == 91) || (sqlType.intValue() == 93))
			return analyseDateValue(value);
		if (sqlType.intValue() == 92) {
			return Time.valueOf(value);
		}
		return null;
	}

	private static Object analyseDateValue(String value) throws ParseException {
		double days;
		String format = "";

		if (value.length() == 19)
			format = "yyyy-MM-dd hh:mm:ss";
		else if (value.length() == 16)
			format = "yyyy-MM-dd hh:mm";
		else {
			format = "yyyy-MM-dd";
		}

		String cleanValue = value.replace(" ", "").toUpperCase();
		Pattern p1 = Pattern.compile("SYSDATE[+-](\\d+[.])?\\d+");
		Matcher m1 = p1.matcher(cleanValue);
		Pattern p2 = Pattern.compile("SYSTIME[+-](\\d+[.])?\\d+");
		Matcher m2 = p2.matcher(cleanValue);

		if (value.equalsIgnoreCase("SYSDATE"))
			return DateUtil.getSystemDate();
		if (value.equalsIgnoreCase("SYSTIME"))
			return DateUtil.getSystemTime();
		if (m1.matches()) {
			days = Double.valueOf(cleanValue.substring(7)).doubleValue();
			return DateUtil.getDateByDays(DateUtil.getSystemDate(), days);
		}
		if (m2.matches()) {
			days = Double.valueOf(cleanValue.substring(7)).doubleValue();
			return new Timestamp(DateUtil.getDateByDays(DateUtil.getSystemTime(), days).getTime());
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return new Timestamp(dateFormat.parse(value).getTime());
	}

	public static String getType(Integer dataType) throws ParseException {
		if ((dataType.intValue() == 1) || (dataType.intValue() == 12) || (dataType.intValue() == -1))
			return "STRING";
		if ((dataType.intValue() == 2) || (dataType.intValue() == 3))
			return "BIG DECIMAL";
		if (dataType.intValue() == -7)
			return "BOOLEAN";
		if (dataType.intValue() == -6)
			return "BYTE";
		if (dataType.intValue() == 5)
			return "SHORT";
		if (dataType.intValue() == 4)
			return "INTEGER";
		if (dataType.intValue() == -5)
			return "LONG";
		if (dataType.intValue() == 7)
			return "FLOAT";
		if ((dataType.intValue() == 6) || (dataType.intValue() == 8))
			return "DOUBLE";
		if ((dataType.intValue() == -2) || (dataType.intValue() == -3) || (dataType.intValue() == -4))
			return "BYTE ARRAY";
		if (dataType.intValue() == 91)
			return "DATE";
		if (dataType.intValue() == 93)
			return "TIMESTAMP";
		if (dataType.intValue() == 92) {
			return "TIME";
		}
		return null;
	}

	public static void populateColumn(Column vo, String dataTypeString) {
		if (dataTypeString == null) {
			return;
		}

		dataTypeString = StringUtil.replaceAll(dataTypeString, " ", "");

		if ("INTEGER".equalsIgnoreCase(dataTypeString)) {
			vo.setDataType(Integer.valueOf(-5));
		} else if ("DATE".equalsIgnoreCase(dataTypeString)) {
			vo.setDataType(Integer.valueOf(91));
		} else if (dataTypeString.startsWith("TIMESTAMP")) {
			vo.setDataType(Integer.valueOf(93));
		} else if (dataTypeString.toUpperCase().contains("CLOB")) {
			vo.setDataType(Integer.valueOf(2005));
		} else if (dataTypeString.toUpperCase().contains("BLOB")) {
			vo.setDataType(Integer.valueOf(2004));
		} else if (dataTypeString.toUpperCase().contains("VARCHAR")) {
			if (!(StringUtil.isMatch("N?VARCHAR2?\\(\\d+\\)", dataTypeString.toUpperCase()))) {
				throw new RuntimeException("Datatype is not correct - " + vo.getColumnName() + ", " + dataTypeString);
			}
			String type = dataTypeString.substring(0, dataTypeString.indexOf("("));
			String length = dataTypeString.substring(dataTypeString.indexOf("(") + 1, dataTypeString.indexOf(")"));
			if ((type.equals("VARCHAR")) || (type.equals("VARCHAR2")))
				vo.setDataType(Integer.valueOf(12));
			else if (type.equals("NVARCHAR2")) {
				vo.setDataType(Integer.valueOf(12));
			}
			vo.setLength(Integer.valueOf(length));
		} else if (dataTypeString.toUpperCase().contains("CHAR")) {
			String length = dataTypeString.substring(dataTypeString.indexOf("(") + 1, dataTypeString.indexOf(")"));
			vo.setDataType(Integer.valueOf(1));
			vo.setLength(Integer.valueOf(length));
		} else {
			if (dataTypeString.toUpperCase().contains("NUMBER")) {
				String precision;
				vo.setDataType(Integer.valueOf(2));
				if ("NUMBER".equals(dataTypeString))
					return;
				if (StringUtil.isMatch("NUMBER\\(\\d+\\)", dataTypeString.toUpperCase())) {
					precision = dataTypeString.substring(dataTypeString.indexOf("(") + 1, dataTypeString.indexOf(")"));
					vo.setPrecision(Integer.valueOf(precision));
					return;
				}
				if (StringUtil.isMatch("NUMBER\\(\\d+,\\d+\\)", dataTypeString.toUpperCase())) {
					precision = dataTypeString.substring(dataTypeString.indexOf("(") + 1, dataTypeString.indexOf(","));
					String scale = dataTypeString.substring(dataTypeString.indexOf(",") + 1,
							dataTypeString.indexOf(")"));
					vo.setPrecision(Integer.valueOf(precision));
					vo.setScale(Integer.valueOf(scale));
					return;
				}
				throw new RuntimeException("Datatype is not correct - " + vo.getColumnName() + ", " + dataTypeString);
			}

			throw new RuntimeException("Type - " + vo.getColumnName() + ", " + dataTypeString + " is not supported.");
		}
	}

	public static String getDataTypeString(Column vo) {
		if ((91 == vo.getDataType().intValue()) || (92 == vo.getDataType().intValue())
				|| (93 == vo.getDataType().intValue()))
			return vo.getTypeName();
		if ((vo.getScale() == null) || (vo.getScale().intValue() == 0)) {
			return vo.getTypeName() + "(" + vo.getLength() + ")";
		}
		return vo.getTypeName() + "(" + vo.getPrecision() + "," + vo.getScale() + ")";
	}

	public static boolean dataTypeIsSupported(Integer dataType) {
		if (dataType == null) {
			logger.warn("Data type is null...");
			return false;
		}

		if ((dataType.intValue() == 2004) || (dataType.intValue() == 2005)) {
			logger.warn("Unsupported data type[" + dataType + "], column will be skipped.");
			return false;
		}

		return true;
	}
}
