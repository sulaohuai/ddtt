package com.ddtt.testdata.dbschema;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.ddtt.testdata.object.Column;
import com.ddtt.testdata.object.Table;
import com.ddtt.testdata.support.DDTTProperties;

public abstract class DBSchemaReader {
	protected Logger logger = Logger.getLogger(this.getClass());
	public static final Pattern UNWANTED_TABLE_PATTERN = Pattern.compile(DDTTProperties.TEST_DATA_UNWANTED_TABLES_REGEX,
			Pattern.CASE_INSENSITIVE);

	public List<Table> getTableList() throws Exception {
		List<Table> result = new ArrayList<Table>();

		while (hasNext()) {
			String tableName = next();

			String[] pks = getPrimaryKeys();
			List<Column> columnList = getColumnList();

			Table table = new Table(tableName);
			table.setPrimaryKeys(pks);
			table.addAllColumn(columnList);

			result.add(table);
		}

		return result;
	}

	public boolean isUnwantedTable(String tableName) {
		if ((DDTTProperties.TEST_DATA_UNWANTED_TABLES_REGEX == null)
				|| ("".equals(DDTTProperties.TEST_DATA_UNWANTED_TABLES_REGEX.trim()))) {
			return false;
		}

		Matcher m = UNWANTED_TABLE_PATTERN.matcher(tableName);
		boolean isUnwanted = m.matches();
		if (isUnwanted) {
			logger.info("Unwanted table: " + tableName);
		}
		return isUnwanted;
	}

	protected abstract boolean hasNext() throws Exception;

	protected abstract String next() throws Exception;

	protected abstract String getTableName() throws Exception;

	protected abstract String[] getPrimaryKeys() throws Exception;

	protected abstract List<Column> getColumnList() throws Exception;
}
