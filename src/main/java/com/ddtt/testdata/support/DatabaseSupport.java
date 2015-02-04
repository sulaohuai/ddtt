package com.ddtt.testdata.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ddtt.testdata.base.TestDataDaoSupport;
import com.ddtt.testdata.base.TestDataService;
import com.ddtt.testdata.object.Column;
import com.ddtt.testdata.object.Table;
import com.ddtt.testdata.object.TestDataRecord;
import com.ddtt.testdata.util.ArrayUtil;
import com.ddtt.testdata.util.BeanUtils;

public class DatabaseSupport implements TestDataService {
	protected Logger logger = Logger.getLogger(this.getClass());

	private Map<String, Table> tables = new HashMap<String, Table>();

	private List<String> clearedTableNameList = new ArrayList<String>();
	private List<String> insertedTableNameList = new ArrayList<String>();
	private TestDataDaoSupport daoSupport;

	private Table getColumnDataType(String tableName) throws Exception {
		if (this.tables.get(tableName) != null) {
			return (this.tables.get(tableName));
		}

		Table table = new Table(tableName);
		List<Column> columnList = DDTTSupport.getDBMetaDataSupport()
				.getColumnList(tableName);
		table.addAllColumn(columnList);

		this.tables.put(tableName, table);
		return table;
	}

	private String preparedSql(String tableName, List<Column> columns)
			throws SQLException {
		StringBuffer sql = new StringBuffer("INSERT INTO ");
		sql.append(tableName);
		sql.append(" (");
		for (Column column : columns) {
			if (!(DataTypeConvertion.dataTypeIsSupported(column.getDataType()))) {
				continue;
			}
			sql.append("\"");
			sql.append(column.getColumnName());
			sql.append("\"");
			sql.append(",");
		}
		sql = new StringBuffer(sql.substring(0, sql.length() - 1));
		sql.append(") VALUES (");
		for (int i = 0; i < columns.size(); ++i) {
			if (!(DataTypeConvertion.dataTypeIsSupported(columns.get(i)
					.getDataType()))) {
				continue;
			}
			sql.append("?");
			sql.append(",");
		}
		sql = new StringBuffer(sql.substring(0, sql.length() - 1));
		sql.append(")");
		return sql.toString();
	}

	public void processTestData(List<Table> sortedTableList, String processType)
			throws Exception {
		if (("C".equalsIgnoreCase(processType))
				|| ("CI".equalsIgnoreCase(processType))) {
			logger.info("=========== Start to clear records ==========");
			try {
				for (int i = sortedTableList.size() - 1; i >= 0; i--) { // clear
																		// child
																		// table
																		// first
					clearTestData(sortedTableList.get(i));
				}
			} catch (Exception e) {
				logger.info("Fail to clear test data.");
				throw e;
			}
			logger.info("=========== Clear records successfully ==========");
		}

		if ((("I".equalsIgnoreCase(processType)))
				|| (("CI".equalsIgnoreCase(processType)))) {
			logger.info("=========== Start to insert records ==========");
			try {
				for (Table t : sortedTableList) { // insert parent table first
					insertTestData(t);
				}
			} catch (Exception e) {
				logger.info("Fail to insert test data.");
				throw e;
			}
			logger.info("=========== Insert records successfully ==========");
		}
	}

	private void clearTestData(Table t) throws Exception {
		if (t == null || this.clearedTableNameList.contains(t.getTableName())) {
			return;
		}

		// To check if it is a valid table
		Table table = DDTTSupport.getDBMetaDataSupport().find(t.getTableName());
		if (table == null) {
			return;
		}

		logger.info("Clear " + t.getTableName() + " starts.");

		int deletedCnt = 0;
		for (TestDataRecord record : t.getRecords()) {
			if ((t.getPrimaryKeys() == null)
					|| (t.getPrimaryKeys().length == 0)) {
				deletedCnt += deleteData(t.getTableName(), t.getColumns(),
						record);
			} else {
				String[] pkValues = getPrimaryKeyValues(t.getPrimaryKeys(),
						record);
				deletedCnt += deleteData(t.getTableName(), t.getPrimaryKeys(),
						pkValues);
			}
		}

		this.clearedTableNameList.add(t.getTableName());
		logger.info("Clear " + t.getTableName() + " finished. " + deletedCnt
				+ " record(s) are deleted.");
	}

	public void insertTestData(Table t) throws Exception {
		if (t == null || this.insertedTableNameList.contains(t.getTableName())) {
			return;
		}

		// To check if it is a valid table
		Table table = DDTTSupport.getDBMetaDataSupport().find(t.getTableName());
		if (table == null) {
			return;
		}

		logger.info("Insert " + t.getTableName() + " starts.");

		int insertedCnt = 0;
		for (TestDataRecord record : t.getRecords()) {
			setDefaultValue(record.getValues(), t.getDefaultValue().getValues());
			insertedCnt += insertTestData(t.getTableName(), t.getColumns(),
					record);
		}

		this.insertedTableNameList.add(t.getTableName());
		logger.info("Insert " + t.getTableName() + " finished. " + insertedCnt
				+ " record(s) are inserted.");
	}

	private void setDefaultValue(Map<String, String> dataRecordValue,
			Map<String, String> defaultValue) {
		try {
			BeanUtils.copyProperties(dataRecordValue, defaultValue, false);
		} catch (Exception e) {
			throw new DDTTException(e);
		}
	}

	public int insertTestData(String tableName, List<Column> columns,
			TestDataRecord record) throws Exception {
		String sql = preparedSql(tableName, columns);
		logger.debug(sql);

		logger.debug("Parameters: ");
		int columnIndex = 1;
		List<Object> objList = new ArrayList<Object>();
		for (int i = 0; i < columns.size(); ++i) {
			Column column = columns.get(i);

			Integer dataType = column.getDataType();

			if (!(DataTypeConvertion.dataTypeIsSupported(dataType))) {
				continue;
			}

			String value = record.getValue(column.getColumnName());
			Object obj = DataTypeConvertion.typeConvert(dataType, value);

			if (obj instanceof Date) {
				logger.debug(columnIndex + " - " + column.getColumnName()
						+ " - " + obj + " - TIMESTAMP/DATE");
				obj = new Timestamp(((Date) obj).getTime());
			} else {
				logger.debug(columnIndex + " - " + column.getColumnName()
						+ " - " + obj + " - "
						+ DataTypeConvertion.getType(dataType));
			}
			objList.add(obj);
			++columnIndex;
		}

		return this.daoSupport.insert(sql, objList.toArray());
	}

	public void disableTriggers(Set<String> tables) throws Exception {
		if (DDTTProperties.TEST_DATA_PROCESS_DISABLE_TRIGGER) {
			for (String table : tables) {
				toggleTriggers(table, false);
			}
		}
	}

	public void enableTriggers(Set<String> tables) throws Exception {
		if (DDTTProperties.TEST_DATA_PROCESS_DISABLE_TRIGGER) {
			for (String table : tables) {
				toggleTriggers(table, true);
			}
		}
	}

	private void toggleTriggers(String tableName, boolean enable) {
		String operation = (enable == true) ? "Enable" : "Disable";
		String sql = "ALTER TABLE " + tableName + " " + operation.toUpperCase()
				+ " ALL TRIGGERS";
		this.daoSupport.execute(sql);
	}

	private String[] getPrimaryKeyValues(String[] pkColumns,
			TestDataRecord record) {
		if ((pkColumns == null) || (pkColumns.length == 0)) {
			return new String[0];
		}

		String[] pkValues = new String[pkColumns.length];
		for (int i = 0; i < pkColumns.length; ++i) {
			for (String columnName : record.getValues().keySet()) {
				if (columnName.equals(pkColumns[i])) {
					pkValues[i] = (record.getValues().get(columnName));
					break;
				}
			}
		}

		return pkValues;
	}

	private int deleteData(String tableName, List<Column> columns,
			TestDataRecord record) throws Exception {
		logger.debug("Delete data from table " + tableName
				+ " (match all columns).");

		StringBuffer sql = new StringBuffer("DELETE FROM ");
		sql.append(tableName);
		sql.append(" WHERE ");
		sql.append(columns.get(0).getColumnName());
		sql.append(" = ? ");
		for (int i = 1; i < columns.size(); ++i) {
			if (!(DataTypeConvertion.dataTypeIsSupported(columns.get(i)
					.getDataType()))) {
				continue;
			}

			sql.append("AND ");
			sql.append(columns.get(i).getColumnName());
			Integer dataType = columns.get(i).getDataType();
			Object obj = DataTypeConvertion.typeConvert(dataType,
					record.getValue(columns.get(i).getColumnName()));
			if (obj == null)
				sql.append(" IS NULL ");
			else {
				sql.append(" = ? ");
			}
		}
		logger.debug(sql);

		int columnIndex = 1;
		int nullCount = 0;
		Object[] objs = new Object[columns.size()];
		for (int j = 0; j < columns.size(); ++j) {
			if (!(DataTypeConvertion.dataTypeIsSupported(columns.get(j)
					.getDataType()))) {
				continue;
			}
			Integer dataType = columns.get(j).getDataType();
			logger.debug(columns.get(j).getColumnName() + " - "
					+ columns.get(j).getDataTypeString() + " - "
					+ columns.get(j).getDataType());
			logger.debug(columnIndex + " - "
					+ record.getValue(columns.get(j).getColumnName()) + " - "
					+ DataTypeConvertion.getType(dataType));
			Object obj = DataTypeConvertion.typeConvert(dataType,
					record.getValue(columns.get(j).getColumnName()));

			if (obj == null)
				++nullCount;
			else {
				objs[(j - nullCount)] = obj;
			}

			++columnIndex;
		}

		Object[] newObjs = new Object[columns.size() - nullCount];
		for (int j = 0; j < columns.size() - nullCount; ++j) {
			newObjs[j] = objs[j];
		}

		return this.daoSupport.delete(sql.toString(), newObjs);
	}

	private int deleteData(String tableName, String[] pkColumnNames,
			String[] pkValues) throws Exception {
		logger.debug("Delete data from table " + tableName);
		logger.debug("Primary key column names: "
				+ ArrayUtil.toString(pkColumnNames));
		logger.debug("Primary key column values: "
				+ ArrayUtil.toString(pkValues));

		Table table = getColumnDataType(tableName);

		if (table == null) {
			throw new Exception("Table " + tableName
					+ " is not found in our DB!");
		}

		StringBuffer sql = new StringBuffer("DELETE FROM ");
		sql.append(tableName);
		sql.append(" WHERE ");
		sql.append(pkColumnNames[0]);
		sql.append(" = ? ");
		for (int i = 1; i < pkColumnNames.length; ++i) {
			sql.append("and ");
			sql.append(pkColumnNames[i]);

			Integer dataType = table.getColumn(pkColumnNames[i]).getDataType();
			Object obj = DataTypeConvertion.typeConvert(dataType, pkValues[i]);
			if (obj == null)
				sql.append(" IS NULL ");
			else {
				sql.append(" = ? ");
			}
		}

		logger.debug(sql);

		// int columnIndex = 1;
		Object[] objs = new Object[pkColumnNames.length];
		for (int i = 0; i < pkColumnNames.length; ++i) {
			Integer dataType = table.getColumn(pkColumnNames[i]).getDataType();
			Object obj = DataTypeConvertion.typeConvert(dataType, pkValues[i]);

			logger.debug(pkColumnNames[i] + " - " + pkValues[i] + " - "
					+ DataTypeConvertion.getType(dataType));
			objs[i] = obj;

			// ++columnIndex;
		}

		return this.daoSupport.delete(sql.toString(), objs);
	}

	public TestDataDaoSupport getDaoSupport() {
		return this.daoSupport;
	}

	public void setDaoSupport(TestDataDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}

	public void executeSqlFile(String fileName) throws Exception {
		File file = new File(fileName);
		if (!file.exists()) {
			throw new Exception(fileName + " does not exists.");
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));

			ArrayList<String> list = new ArrayList<String>();
			String line = reader.readLine();
			while (line != null) {
				line = line.trim();
				// to support ";" at the end of the sql
				if (";".equals(line.substring(line.length() - 1, line.length()))) {
					line = line.substring(0, line.length() - 1);
				}
				
				// to support comments
				StringBuilder sbf = new StringBuilder(line);
				while (sbf.charAt(0) == ' ') {
					sbf = sbf.deleteCharAt(0);
				}
				line = sbf.toString();
				
				if(!line.startsWith("--")){
					list.add(line);
				}
				
				line = reader.readLine();
			}

			if (list.size() > 0) {
				String[] lines = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					lines[i] = list.get(i);
				}
				daoSupport.batchUpdate(lines);
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
}
