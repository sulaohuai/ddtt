package com.ddtt.testdata.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.ddtt.testdata.object.Table;
import com.ddtt.testdata.object.TestDataRecord;
import com.ddtt.testdata.support.excel.AbstractExcelSupport;

public class ExcelReader {
	protected Logger logger = Logger.getLogger(this.getClass());

	public static String TEST_DATA_TABLE_NAME_SUFFIX_SEPERATOR = "#";
	private final AbstractExcelSupport testDataExcelSupport;

	public ExcelReader(AbstractExcelSupport testDataExcelSupport) {
		this.testDataExcelSupport = testDataExcelSupport;
	}

	public List<String> getSheetNameList() throws Exception {
		List<String> sheetNameList = new ArrayList<String>();
		for (int sheetId = 0; sheetId < this.testDataExcelSupport.getNumberOfSheets(); ++sheetId) {
			String sheetName = this.testDataExcelSupport.getSheetName(sheetId);
			if (this.testDataExcelSupport.accept(sheetName)) {
				sheetNameList.add(sheetName);
			}
		}
		Collections.sort(sheetNameList);

		return sheetNameList;
	}

	public List<String> getTableNameList() throws Exception {
		List<String> tableNameList = new ArrayList<String>();

		for (int sheetId = 0; sheetId < this.testDataExcelSupport.getNumberOfSheets(); ++sheetId) {
			String sheetName = this.testDataExcelSupport.getSheetName(sheetId);
			if ((this.testDataExcelSupport.accept(sheetName)) && (!(isAdditionalTableSheet(sheetName)))) {
				tableNameList.add(sheetName);
			}
		}

		return tableNameList;
	}

	public List<String> getSheetNameList(String tableName) throws Exception {
		List<String> sheetNameList = new ArrayList<String>();

		if (!(this.testDataExcelSupport.isSheetExist(tableName))) {
			logger.info("Sheet does not exist - " + tableName);
		} else {
			sheetNameList.add(tableName);

			int index = 1;

			while (this.testDataExcelSupport.isSheetExist(tableName + TEST_DATA_TABLE_NAME_SUFFIX_SEPERATOR + index)) {
				sheetNameList.add(tableName + TEST_DATA_TABLE_NAME_SUFFIX_SEPERATOR + index);
				++index;
			}
		}

		return sheetNameList;
	}

	/**
	 * Get records from Excel for a specific version
	 * 
	 * @param tableName
	 * @param version
	 * @return
	 * @throws Exception
	 */
	public List<TestDataRecord> getRecordList(String tableName, String version) throws Exception {
		List<TestDataRecord> result = new ArrayList<TestDataRecord>();

		List<String> sheetNameList = getSheetNameList(tableName);
		for (String sheetName : sheetNameList) {
			ExcelReader4Sheet testDataExcelReader4Sheet = new ExcelReader4Sheet(this.testDataExcelSupport, sheetName);
			List<TestDataRecord> recordList = testDataExcelReader4Sheet.getRecordList(version);
			result.addAll(recordList);
		}

		return result;
	}

	/**
	 * Get default value for a table, the default value of the first sheet will
	 * be return if there are multiple sheets for a table
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public TestDataRecord getDefaultValue(String tableName) throws Exception {
		List<String> sheetNameList = getSheetNameList(tableName);

		String firstSheetName = sheetNameList.get(0);

		ExcelReader4Sheet testDataExcelReader4Sheet = new ExcelReader4Sheet(this.testDataExcelSupport, firstSheetName);

		return testDataExcelReader4Sheet.getDefaultValue();
	}

	public Table getTable(String tableName) throws Exception {
		List<String> sheetNameList = getSheetNameList(tableName);
		if ((sheetNameList == null) || (sheetNameList.size() == 0)) {
			return null;
		}

		ExcelReader4Sheet testDataExcelReader4Sheet = new ExcelReader4Sheet(this.testDataExcelSupport,
				sheetNameList.get(0));

		testDataExcelReader4Sheet.getTable();

		return testDataExcelReader4Sheet.getTable();
	}

	private boolean isAdditionalTableSheet(String sheetName) throws Exception {
		return ((this.testDataExcelSupport.accept(sheetName)) && (sheetName
				.contains(TEST_DATA_TABLE_NAME_SUFFIX_SEPERATOR)));
	}
}
