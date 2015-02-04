package com.ddtt.testdata.support;

public class DDTTConstants {
	public static interface ExcelVersion {
		public Integer EXCEL_2003 = 2003;
		public Integer EXCEL_2007 = 2007;
	}

	public static interface ExcelMaxColumnCount {
		public Integer EXCEL_MAX_COLUMN_COUNT_2003 = 255;
		public Integer EXCEL_MAX_COLUMN_COUNT_2007 = 1000;
	}

	public static interface ExcelConfig {
		public Integer TEST_DATA_EXCEL_BODY_DATA_START_ROW_INDEX = 4;
		public Integer TEST_DATA_EXCEL_BODY_DATA_START_COLUMN_INDEX = 6;
		public Integer TEST_DATA_EXCEL_BODY_DATA_DEFAULTVALUE_COLUMN_INDEX = 4;

		public String[] TEST_DATA_DATABASE_ATTRIBUTE = new String[] { "FIELDNAME", "DATATYPE", "NULLABLE", "COMMENTS",
				"DEFAULTVALUE" };
		public String[] TEST_DATA_EXCEL_SCHEMA_COLUMNS_HEADER = new String[] { "FIELD NAME", "TYPE", "NULLABLE (Y/N)",
				"COMMENTS", "DEFAULT VALUE" };

		public Integer[] TEST_DATA_EXCEL_BODY_COLUMNS_WIDTH = new Integer[] { 4000, 3000, 2500, 7000, 3000};
		public Integer[] TEST_DATA_EXCEL_BODY_COLUMNS_INDEX = new Integer[] { 0, 1, 2, 3, 4};

		public Integer TEST_DATA_EXCEL_BODY_FIELDNAME_INDEX = 0;

		public Integer[][] TEST_DATA_EXCEL_HEADER_TABLE_NAME_CELLS = new Integer[][] { { 0, 0 }, { 0, 1 } };
		public Integer[][] TEST_DATA_EXCEL_HEADER_PK_CELLS = new Integer[][] { { 1, 0 }, { 1, 1 } };
	}

}
