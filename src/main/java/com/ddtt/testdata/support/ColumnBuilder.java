package com.ddtt.testdata.support;

import com.ddtt.testdata.object.Column;
import com.ddtt.testdata.util.StringUtil;

public class ColumnBuilder {
	public static void populateColumn(Column vo, String propertyOfDatabaseColumn, String value) {
		if (StringUtil.isEmpty(value)) {
			return;
		}

		if (COLUMN_PROPERTIES.FIELDNAME.toString().equals(propertyOfDatabaseColumn)) {
			vo.setColumnName(value);
		} else if (COLUMN_PROPERTIES.DATATYPE.toString().equals(propertyOfDatabaseColumn)) {
			vo.setDataTypeString(value);
			DataTypeConvertion.populateColumn(vo, value);
		} else if (COLUMN_PROPERTIES.NULLABLE.toString().equals(propertyOfDatabaseColumn)) {
			vo.setNullable(Boolean.valueOf(value));
		} else if (COLUMN_PROPERTIES.COMMENTS.toString().equals(propertyOfDatabaseColumn)) {
			vo.setComments(value);
		}
	}

	public static String getColumnProperty(Column vo, String propertyOfDatabaseColumn) {
		if (COLUMN_PROPERTIES.FIELDNAME.toString().equals(propertyOfDatabaseColumn)){
			return vo.getColumnName();
		} else if (COLUMN_PROPERTIES.DATATYPE.toString().equals(propertyOfDatabaseColumn)){
			return vo.getDataTypeString();
		} else if (COLUMN_PROPERTIES.NULLABLE.toString().equals(propertyOfDatabaseColumn)){
			return ((Boolean.TRUE.equals(vo.getNullable())) ? "Y" : "N");
		}else if (COLUMN_PROPERTIES.COMMENTS.toString().equals(propertyOfDatabaseColumn)) {
			return vo.getComments();
		}else if (COLUMN_PROPERTIES.DEFAULTVALUE.toString().equals(propertyOfDatabaseColumn)) {
			return "";
		}
		throw new RuntimeException("Property of database column is not supported: " + propertyOfDatabaseColumn);
	}

	public static enum COLUMN_PROPERTIES {
		FIELDNAME, DATATYPE, NULLABLE, COMMENTS, DEFAULTVALUE;
	}
}
