package com.ddtt.testdata.object;

import com.ddtt.testdata.support.DataTypeConvertion;

public class Column {
	private String columnName;
	private String dataTypeString;
	private Integer dataType;
	private String typeName;
	private Integer length;
	private Boolean nullable;
	private String comments;
	private Integer precision;
	private Integer scale;
	private Object defaultValue;

	public Column(String columnName) {
		this.columnName = columnName;
	}

	public String getDataTypeString() {
		if ((this.dataTypeString != null) && (!("".equals(this.dataTypeString)))) {
			return this.dataTypeString;
		}

		return DataTypeConvertion.getDataTypeString(this);
	}

	public String getColumnName() {
		return this.columnName;
	}

	public void setDataTypeString(String dataTypeString) {
		this.dataTypeString = dataTypeString;
	}

	public Integer getPrecision() {
		return this.precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	public Integer getScale() {
		return this.scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Integer getDataType() {
		return this.dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public Integer getLength() {
		return this.length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Boolean getNullable() {
		return this.nullable;
	}

	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
}