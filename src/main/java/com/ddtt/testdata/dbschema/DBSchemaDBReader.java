package com.ddtt.testdata.dbschema;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ddtt.testdata.base.Filter;
import com.ddtt.testdata.object.Column;
import com.ddtt.testdata.object.Table;
import com.ddtt.testdata.support.DDTTSupport;

public class DBSchemaDBReader extends DBSchemaReader {
	protected Logger logger = Logger.getLogger(this.getClass());

	private List<Table> tableList = new ArrayList<Table>();
	private int tableIndex = -1;

	public DBSchemaDBReader() {
		logger.info("Load tables from database to memory");
		try {
			loadTables();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected List<Column> getColumnList() throws Exception {
		return this.tableList.get(this.tableIndex).getColumns();
	}

	@Override
	protected String[] getPrimaryKeys() throws Exception {
		return this.tableList.get(this.tableIndex).getPrimaryKeys();
	}

	@Override
	protected String getTableName() throws Exception {
		return this.tableList.get(this.tableIndex).getTableName();
	}

	@Override
	protected boolean hasNext() throws Exception {
		return (this.tableIndex + 1 < this.tableList.size());
	}

	private void loadTables() throws SQLException {
		this.tableList = DDTTSupport.getDBMetaDataSupport().getTableList(new Filter() {
			public boolean filtered(String tableName) {
				return DBSchemaDBReader.this.isUnwantedTable(tableName);
			}
		});
	}

	@Override
	protected String next() throws Exception {
		this.tableIndex += 1;
		return getTableName();
	}
}
