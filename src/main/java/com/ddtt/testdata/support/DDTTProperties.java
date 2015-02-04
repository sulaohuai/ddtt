package com.ddtt.testdata.support;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.ddtt.testdata.object.Table;
import com.ddtt.testdata.util.StringUtil;

public class DDTTProperties {
	public static Table[] TEST_DATA_PROCESS_LISTED_TABLES;
	public static String TEST_DATA_PROCESS_TYPE;
	public static boolean TEST_DATA_TAKE_CARE_OF_CHILD_TABLE;
	public static boolean TEST_DATA_PROCESS_DISABLE_TRIGGER;
	public static String DB_SCHEMA_FILE_PATH;
	public static String TEST_DATA_FILE_PATH;
	public static String TEST_DATA_SQL_BEFORE;
	public static String TEST_DATA_SQL_AFTER;
	public static int TEST_DATA_EXCEL_VERSION;
	public static int TEST_DATA_EXCEL_MAX_COLUMN_COUNT;
	public static String TEST_DATA_TEMPLATE_READER;
	public static String TEST_DATA_UNWANTED_TABLES_REGEX;
	public static String DB_CONNECTION_URL;
	public static String DB_USER_NAME;
	public static String DB_PASSWORD;
	public static String DB_DRIVER_NAME;
	public static String DB_SCHEMA;
	public static String[] TABLE_PREFIX;
	public static String TEST_DATA_EXCEL_INDEX_SHEET_NAME;
	public static String TEST_DATA_EXCEL_PROTECTION_PASSWORD;
	protected static Configure properties = Configure.parse("test-data-config.properties");

	public static void initPropConstants(Properties props) {
		if ("all".equalsIgnoreCase(props.getProperty("test.data.process.listed.tables.only"))) {
			TEST_DATA_PROCESS_LISTED_TABLES = null;
		} else {
			String listedTables = StringUtil.replaceAll(props.getProperty("test.data.process.listed.tables.only"), " ",
					"");
			listedTables = StringUtil.replaceAll(listedTables, ")", "");
			listedTables = StringUtil.replaceAll(listedTables, "(", "=");
			String[] listedTablesArray = StringUtil.split(listedTables, ",");
			TEST_DATA_PROCESS_LISTED_TABLES = new Table[listedTablesArray.length];
			for (int i = 0; i < listedTablesArray.length; ++i) {
				String[] tableInfoArray = StringUtil.split(listedTablesArray[i], "=");
				Table table = new Table(tableInfoArray[0]);
				if (tableInfoArray.length > 1) {
					table.setDataVersion(tableInfoArray[1]);
				}
				TEST_DATA_PROCESS_LISTED_TABLES[i] = table;
			}
			if ((TEST_DATA_PROCESS_LISTED_TABLES == null) || (TEST_DATA_PROCESS_LISTED_TABLES.length == 0)) {
				throw new RuntimeException("test.data.process.listed.tables.only"
						+ " is not specified in test-data-config.properties.");
			}
		}

		TEST_DATA_PROCESS_TYPE = props.getProperty("test.data.process.type");
		TEST_DATA_TAKE_CARE_OF_CHILD_TABLE = "true".equalsIgnoreCase(props
				.getProperty("test.data.take.care.of.child.table"));
		TEST_DATA_PROCESS_DISABLE_TRIGGER = "true".equalsIgnoreCase(props
				.getProperty("test.data.process.disable.trigger"));

		TEST_DATA_FILE_PATH = props.getProperty("test.data.file.path");
		
		TEST_DATA_SQL_BEFORE = props.getProperty("test.data.sql.before");
		TEST_DATA_SQL_AFTER = props.getProperty("test.data.sql.after");

		TEST_DATA_EXCEL_VERSION = new Integer(props.getProperty("excel.version"));

		TEST_DATA_EXCEL_MAX_COLUMN_COUNT = new Integer(props.getProperty("excel.max.column.count"));

		TEST_DATA_TEMPLATE_READER = props.getProperty("test.data.template.reader");

		TEST_DATA_UNWANTED_TABLES_REGEX = props.getProperty("test.data.unwanted.tables.regex");

		initTablePrefix(props);

		DB_CONNECTION_URL = props.getProperty("jdbc.db.url");
		DB_USER_NAME = props.getProperty("jdbc.db.user.name");
		DB_PASSWORD = props.getProperty("jdbc.db.user.password");
		DB_DRIVER_NAME = props.getProperty("db.drive.name");
		DB_SCHEMA = props.getProperty("jdbc.db.schema");
		DB_SCHEMA = (DB_SCHEMA != null) ? DB_SCHEMA.toUpperCase() : null;

		TEST_DATA_EXCEL_INDEX_SHEET_NAME = props.getProperty("test.data.excel.index.sheet.name");
		TEST_DATA_EXCEL_PROTECTION_PASSWORD = props.getProperty("test.data.excel.protection.password");
	}

	private static void initTablePrefix(Properties props) {
		String prefixStr = props.getProperty("table.prefix");
		if (StringUtil.isNotEmpty(prefixStr)) {
			String[] prefixes = prefixStr.split(",");
			for (int i = 0; i < prefixes.length; ++i) {
				prefixes[i] = prefixes[i].toUpperCase().trim();
			}
			TABLE_PREFIX = prefixes;
		}
	}

	static {
		initPropConstants(properties);

		Logger logger = Logger.getLogger(DDTTProperties.class);
		logger.info("########## Configurations ##########");
		logger.info("Excel version: " + TEST_DATA_EXCEL_VERSION);
		logger.info("Tables and versions: " + properties.getProperty("test.data.process.listed.tables.only"));
		logger.info("Processed tables: " + properties.getProperty("table.prefix"));
		logger.info("Ignored tables: " + TEST_DATA_UNWANTED_TABLES_REGEX);
		logger.info("Process child tables: " + TEST_DATA_TAKE_CARE_OF_CHILD_TABLE);
		logger.info("Disable triggers: " + TEST_DATA_PROCESS_DISABLE_TRIGGER);
		logger.info("########## Configurations ##########");
	}
}
