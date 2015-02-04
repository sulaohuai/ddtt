package com.ddtt.testdata.main;

import org.apache.log4j.Logger;

import com.ddtt.testdata.dbschema.DBSchemaDBReader;
import com.ddtt.testdata.dbschema.DBSchemaReader;
import com.ddtt.testdata.process.ExcelReader;
import com.ddtt.testdata.process.ExcelWriter;
import com.ddtt.testdata.support.DDTTProperties;
import com.ddtt.testdata.support.DDTTSupport;
import com.ddtt.testdata.support.excel.AbstractExcelSupport;
import com.ddtt.testdata.support.excel.ExcelSupportFactory;

public class TestDataTemplateSynchronizer {
	protected Logger logger = Logger.getLogger(this.getClass());

	public void generateTemplate() throws Exception {
		logger.info("########## Start to sync table structure ##########");
		logger.info("Test Data: " + DDTTProperties.TEST_DATA_FILE_PATH);

		if (!DDTTSupport.propertyValidation()) {
			return;
		}

		AbstractExcelSupport testDataExcelSupport = ExcelSupportFactory.getExcelSupport();
		ExcelReader testDataExcelReader = new ExcelReader(testDataExcelSupport);
		ExcelWriter testDataExcelWriter = new ExcelWriter(testDataExcelSupport, testDataExcelReader);

		DBSchemaReader dbSchemaReader = new DBSchemaDBReader();
		testDataExcelWriter.write(dbSchemaReader.getTableList());

		logger.info("########## Table structure is sync successfully. ##########");
	}

	public static void main(String[] args) throws Exception {
		TestDataTemplateSynchronizer sync = new TestDataTemplateSynchronizer();
		sync.generateTemplate();
	}
}
