package com.ddtt.testdata.object;

import java.util.HashMap;
import java.util.Map;

public class TestDataRecord {
	private String remarks;
	private Map<String, String> values;

	public TestDataRecord() {
		this.values = new HashMap<String, String>();
	}

	public void putValue(String field, String value) {
		this.values.put(field, value);
	}

	public String getValue(String columnName) {
		return (this.values.get(columnName));
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Map<String, String> getValues() {
		return this.values;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[Test Data Remarks=");
		sb.append(this.remarks);
		sb.append("], ");

		for (String key : this.values.keySet()) {
			sb.append(key);
			sb.append("=");
			sb.append(this.values.get(key));
			sb.append(", ");
		}

		return sb.toString();
	}
}