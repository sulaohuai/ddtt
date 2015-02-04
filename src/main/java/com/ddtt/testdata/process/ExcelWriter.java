package com.ddtt.testdata.process;

import java.util.List;

import org.apache.log4j.Logger;

import com.ddtt.testdata.object.Column;
import com.ddtt.testdata.object.Table;
import com.ddtt.testdata.support.ColumnBuilder;
import com.ddtt.testdata.support.DDTTConstants;
import com.ddtt.testdata.support.DDTTProperties;
import com.ddtt.testdata.support.excel.AbstractExcelSupport;
import com.ddtt.testdata.support.excel.AbstractStyleManager;
import com.ddtt.testdata.util.ArrayUtil;

public class ExcelWriter {
	protected Logger logger = Logger.getLogger(super.getClass());
	private final AbstractExcelSupport testDataExcelSupport;
	private final ExcelReader testDataExcelReader;

	public ExcelWriter(AbstractExcelSupport testDataExcelSupport, ExcelReader testDataExcelReader) {
		this.testDataExcelSupport = testDataExcelSupport;
		this.testDataExcelReader = testDataExcelReader;
	}

	public void write(List<Table> tableList) throws Exception {
		logger.info("=========== Start to sync all tables ... ===========");
		for (Table table : tableList) {
			syncTable(table);
		}
		logger.info("=========== End to sync all tables ===========");

		logger.info("All test data cells are protected, password is '"
				+ DDTTProperties.TEST_DATA_EXCEL_PROTECTION_PASSWORD + "'");

		updateMasterConfig();

		this.testDataExcelSupport.writeToFile();
	}

	private void syncTable(Table table) throws Exception {
		logger.info("To sync the sheet " + table.getTableName());

		createSheetIfNotExists(table.getTableName());
		writeHeader(table.getTableName(), table.getPrimaryKeys());
		writeColumns(table.getTableName(), table.getColumns());
		createColumnSperator(table.getTableName());
		updateTestDataBodyStyle(table.getTableName(), table.getColumns().size());
		protectTableSheet(table.getTableName());
	}

	private void writeHeader(String tableName, String[] primaryKeys) throws Exception {
		List<String> sheetNameList = this.testDataExcelReader.getSheetNameList(tableName);

		for (String sheetName : sheetNameList) {
			int tableNameLabelRowId = DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_HEADER_TABLE_NAME_CELLS[0][0].intValue();
			int tableNameLabelColumnId = DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_HEADER_TABLE_NAME_CELLS[0][1].intValue();
			int tableNameRowId = DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_HEADER_TABLE_NAME_CELLS[1][0].intValue();
			int tableNameColumnId = DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_HEADER_TABLE_NAME_CELLS[1][1].intValue();
			int pkLabelRowId = DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_HEADER_PK_CELLS[0][0].intValue();
			int pkLabelColumnId = DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_HEADER_PK_CELLS[0][1].intValue();
			int pkRowId = DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_HEADER_PK_CELLS[1][0].intValue();
			int pkColumnId = DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_HEADER_PK_CELLS[1][1].intValue();

			this.testDataExcelSupport.createCell(sheetName, tableNameLabelRowId, tableNameLabelColumnId, "Table Name",
					AbstractStyleManager.Style.TABLE_HEADER);
			this.testDataExcelSupport.createCell(sheetName, tableNameRowId, tableNameColumnId, tableName,
					AbstractStyleManager.Style.TABLE_HEADER);
			this.testDataExcelSupport.createCell(sheetName, pkLabelRowId, pkLabelColumnId, "Primary Key Field(s)",
					AbstractStyleManager.Style.TABLE_HEADER);
			this.testDataExcelSupport.createCell(sheetName, pkRowId, pkColumnId, ArrayUtil.toString(primaryKeys),
					AbstractStyleManager.Style.TABLE_HEADER);
		}
	}

	private void writeColumns(String tableName, List<Column> columns) throws Exception {
		if ((columns == null) || (columns.size() == 0)) {
			return;
		}

		List<String> sheetNameList = this.testDataExcelReader.getSheetNameList(tableName);

		for (String sheetName : sheetNameList) {
			int rowIndex;
			for (int i = 0; i < DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_COLUMNS_INDEX.length; ++i) {
				this.testDataExcelSupport.setColumnWidth(sheetName,
						DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_COLUMNS_INDEX[i].intValue(),
						DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_COLUMNS_WIDTH[i].intValue());
			}

			for (int i = 0; i < DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_COLUMNS_INDEX.length; ++i) {
				rowIndex = DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_DATA_START_ROW_INDEX.intValue();
				int columnIndex = DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_COLUMNS_INDEX[i].intValue();
				String header = DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_SCHEMA_COLUMNS_HEADER[i];
				this.testDataExcelSupport.createCell(sheetName, rowIndex, columnIndex, header,
						AbstractStyleManager.Style.COLUMN_HEADER);
			}

			for (int i = 0; i < columns.size(); ++i) {
				rowIndex = DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_DATA_START_ROW_INDEX.intValue() + 1 + i;

				for (int j = 0; j < DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_COLUMNS_INDEX.length; ++j) {
					int columnIndex = DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_COLUMNS_INDEX[j].intValue();
					String value = ColumnBuilder.getColumnProperty(columns.get(i),
							DDTTConstants.ExcelConfig.TEST_DATA_DATABASE_ATTRIBUTE[j]);

					this.testDataExcelSupport.createCell(sheetName, rowIndex, columnIndex, value,
							AbstractStyleManager.Style.COLUMN_PROPERTY);
				}
			}
		}
	}

	private void createColumnSperator(String tableName) throws Exception {
		List<String> sheetNameList = this.testDataExcelReader.getSheetNameList(tableName);

		int columnIndex = DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_DATA_START_COLUMN_INDEX.intValue() - 1;

		for (String sheetName : sheetNameList) {
			this.testDataExcelSupport.setColumnWidth(sheetName, columnIndex, 300);

			for (int rowIndex = 0; rowIndex < 300; ++rowIndex)
				this.testDataExcelSupport.createCell(sheetName, rowIndex, columnIndex, null,
						AbstractStyleManager.Style.COLUMN_SEPERATOR);
		}
	}

	private void updateTestDataBodyStyle(String tableName, int columnCount) throws Exception {
		List<String> sheetNameList = this.testDataExcelReader.getSheetNameList(tableName);

		int startColumnNo = DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_DATA_START_COLUMN_INDEX.intValue();
		int endColumnNo = DDTTProperties.TEST_DATA_EXCEL_MAX_COLUMN_COUNT;
		int startRowNo = DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_DATA_START_ROW_INDEX.intValue();
		int endRowNo = startRowNo + columnCount;

		for (String sheetName : sheetNameList) {
			// update style for default value
			for (int rowIndex = startRowNo+1; rowIndex <= endRowNo; ++rowIndex) {
				this.testDataExcelSupport.setCellStyle(sheetName, rowIndex,
						DDTTConstants.ExcelConfig.TEST_DATA_EXCEL_BODY_DATA_DEFAULTVALUE_COLUMN_INDEX,
						AbstractStyleManager.Style.DEFAULTVALUE);
			}

			// update style for test data body
			for (int rowIndex = startRowNo; rowIndex <= endRowNo; ++rowIndex) {
				for (int columnIndex = startColumnNo; columnIndex <= endColumnNo; ++columnIndex) {
					this.testDataExcelSupport.setCellStyle(sheetName, rowIndex, columnIndex,
							AbstractStyleManager.Style.DATA_CELL);
				}
			}
		}
	}

	private void protectTableSheet(String tableName) throws Exception {
		List<String> sheetNameList = this.testDataExcelReader.getSheetNameList(tableName);

		for (String sheetName : sheetNameList)
			this.testDataExcelSupport.protectSheet(sheetName, DDTTProperties.TEST_DATA_EXCEL_PROTECTION_PASSWORD);
	}

	private void updateMasterConfig() throws Exception {
		logger.info("Updating the Master Config sheet ...");

		String masterConfigSheetName = DDTTProperties.TEST_DATA_EXCEL_INDEX_SHEET_NAME;
		int columnIndex = 0;
		int bodyStartRowId = 1;

		// create cell and clear cell value;
		for (int rowIndex = bodyStartRowId; rowIndex < 300; rowIndex++) {
			testDataExcelSupport.createCell(masterConfigSheetName, rowIndex, columnIndex, null, null);
		}

		int rowIndex = bodyStartRowId;
		for (String sheetName : testDataExcelReader.getSheetNameList()) {
			String formula = "HYPERLINK(\"" + "#" + sheetName + "!A1\",\"" + sheetName + "\")";
			testDataExcelSupport.createCell(masterConfigSheetName, rowIndex, columnIndex, sheetName,
					AbstractStyleManager.Style.HYPERLINK, formula);
			rowIndex++;
		}
	}

	private void createSheetIfNotExists(String tableName) throws Exception {
		if (!(this.testDataExcelSupport.isSheetExist(tableName))) {
			this.testDataExcelSupport.createSheet(tableName);
		}
	}
}
