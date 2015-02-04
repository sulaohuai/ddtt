package com.ddtt.testdata.process;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ddtt.testdata.object.Column;
import com.ddtt.testdata.object.Table;
import com.ddtt.testdata.object.TestDataRecord;
import com.ddtt.testdata.support.ColumnBuilder;
import com.ddtt.testdata.support.DDTTConstants;
import com.ddtt.testdata.support.excel.AbstractExcelSupport;
import com.ddtt.testdata.util.StringUtil;

public class ExcelReader4Sheet {
	protected Logger logger = Logger.getLogger(this.getClass());
	private AbstractExcelSupport testDataExcelSupport;
	private String sheetName;
	private Table table;

	public ExcelReader4Sheet(AbstractExcelSupport testDataExcelReader, String sheetName) throws Exception {
		this.testDataExcelSupport = testDataExcelReader;
		this.sheetName = sheetName;
		getTable();
	}

	public TestDataRecord getDefaultValue() throws Exception {
		logger.debug("Get default value of sheet=" + this.sheetName);

		int columnNo = DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_DATA_DEFAULTVALUE_COLUMN_INDEX;
		int startRowNo = DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_DATA_START_ROW_INDEX.intValue() + 1;
		int endRowNo = startRowNo + getTable().getColumns().size();

		TestDataRecord vo = getTestDataRecord(columnNo, startRowNo, endRowNo);

		return vo;
	}

	private TestDataRecord getTestDataRecord(int columnNo, int startRowNo, int endRowNo) throws Exception {
		TestDataRecord vo = new TestDataRecord();
		int i = startRowNo;
		for (int columnIndex = 0; i < endRowNo; ++columnIndex) {
			String value = this.testDataExcelSupport.getCellValue(this.sheetName, i, columnNo);

			vo.putValue(this.table.getColumn(columnIndex).getColumnName(), value);

			++i;
		}

		String remarks = this.testDataExcelSupport.getCellValue(this.sheetName,
				DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_DATA_START_ROW_INDEX.intValue(), columnNo);

		if (remarks == null) {
			remarks = "";
		}
		vo.setRemarks(remarks);

		logger.debug(vo.toString());
		return vo;
	}

	public List<TestDataRecord> getRecordList(String version) throws Exception {
		logger.info("Get record list of sheet=" + this.sheetName + ", version=" + (version == null ? "all" : version));

		List<TestDataRecord> result = new ArrayList<TestDataRecord>();
		int columnNo = DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_DATA_START_COLUMN_INDEX.intValue();
		int startRowNo = DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_DATA_START_ROW_INDEX.intValue() + 1;
		int endRowNo = startRowNo + getTable().getColumns().size();
		while (true) {
			TestDataRecord vo = getTestDataRecord(columnNo, startRowNo, endRowNo);

			if (isBlankRecord(vo)) {
				break;
			}
			
			if ((version != null) && (!(vo.getRemarks().startsWith(version)))) {
				++columnNo;
				continue; // 20140209
			}

			result.add(vo);
			++columnNo;

			logger.debug(vo.toString());
		}

		return result;
	}

	private boolean isBlankRecord(TestDataRecord vo) {
		for (String value : vo.getValues().values()) {
			if (!(StringUtil.isEmpty(value))) {
				return false;
			}
		}
		return true;
	}

	public Table getTable() throws Exception {
		if (this.table != null) {
			return this.table;
		}

		this.table = new Table(getTableName());
		this.table.addAllColumn(getColumnList());
		this.table.setPrimaryKeys(getPrimaryKeys());

		return this.table;
	}

	private int getBodyRowStart() throws Exception {
		for (int row = 0; row < 100; ++row) {
			String value = this.testDataExcelSupport.getCellValue(this.sheetName, row,
					DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_COLUMNS_INDEX[DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_FIELDNAME_INDEX
							.intValue()].intValue());

			if ((value != null)
					&& (DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_SCHEMA_COLUMNS_HEADER[DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_FIELDNAME_INDEX
							.intValue()].equalsIgnoreCase(value.trim()))) {
				return row;
			}
		}

		throw new RuntimeException("Excel body is not properly configured in " + getTableName() + "!");
	}

	public String getTableName() throws Exception {
		return StringUtil.split(this.sheetName, ExcelReader.TEST_DATA_TABLE_NAME_SUFFIX_SEPERATOR)[0];
	}

	private String[] getPrimaryKeys() throws Exception {
		if (getTable().getPrimaryKeys() != null) {
			return getTable().getPrimaryKeys();
		}

		String s = this.testDataExcelSupport.getCellValue(this.sheetName,
				DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_HEADER_PK_CELLS[1][0].intValue(),
				DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_HEADER_PK_CELLS[1][1].intValue());

		if (StringUtil.isEmpty(s)) {
			return new String[0];
		}

		s = StringUtil.replaceAll(s, " ", "");
		getTable().setPrimaryKeys(StringUtil.split(s, ","));

		return getTable().getPrimaryKeys();
	}

	private List<Column> getColumnList() throws Exception {
		Integer[] columnsIndex = DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_COLUMNS_INDEX;
		int row = getBodyRowStart() + 1;
		int filedNameColumn = columnsIndex[DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_FIELDNAME_INDEX.intValue()].intValue();

		List<Column> columnList = new ArrayList<Column>();

		while (!(StringUtil.isEmpty(this.testDataExcelSupport.getCellValue(this.sheetName, row, filedNameColumn)))) {
			boolean isStrikeout = this.testDataExcelSupport.isStrikeout(this.sheetName, row, filedNameColumn);

			if (isStrikeout) {
				continue;
			}

			Column column = new Column("");
			for (int i = 0; i < columnsIndex.length; ++i) {
				String value = this.testDataExcelSupport.getCellValue(this.sheetName, row, columnsIndex[i].intValue());
				String propertyOfDatabaseColumn = DDTTConstants.ExcelConfig.TEST_DATA_DATABASE_ATTRIBUTE[i];
				ColumnBuilder.populateColumn(column, propertyOfDatabaseColumn, value);
			}
			columnList.add(column);

			++row;
		}

		return columnList;
	}

	public static void main(String[] args) {
		System.out.println(StringUtil.isMatch("NUMBER\\(\\d+\\)", "NUMBER(2)"));
		System.out.println(StringUtil.isMatch("NUMBER\\(\\d+,\\d+\\)", "NUMBER(1232,1213)"));
		System.out.println(StringUtil.isMatch("NUMBER\\(\\d+,\\d+\\)", "NUMBER(2,133213)"));
	}
}
