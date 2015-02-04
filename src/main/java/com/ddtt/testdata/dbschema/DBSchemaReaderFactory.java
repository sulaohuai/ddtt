package com.ddtt.testdata.dbschema;

import org.apache.log4j.Logger;

public class DBSchemaReaderFactory {
	private static Logger logger = Logger.getLogger(DBSchemaReaderFactory.class);

	public static DBSchemaReader createDBSchemaReader(String dbSchemaReader) throws Exception {
		logger.info("DBSchemaReader: " + dbSchemaReader);
		Class<?> clazz = Class.forName(dbSchemaReader);
		return ((DBSchemaReader) clazz.newInstance());
	}
}
