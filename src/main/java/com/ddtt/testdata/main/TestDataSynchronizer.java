package com.ddtt.testdata.main;

import org.apache.log4j.Logger;

import com.ddtt.testdata.process.DatabaseProcessor;
import com.ddtt.testdata.support.DDTTProperties;
import com.ddtt.testdata.support.DDTTSupport;
import com.ddtt.testdata.support.excel.AbstractExcelSupport;
import com.ddtt.testdata.support.excel.ExcelSupportFactory;

public class TestDataSynchronizer {
	protected Logger logger = Logger.getLogger(this.getClass());

	public void synchronizeData() throws Exception {
		logger.info("########## Start to sync test data ##########");
		logger.info("Test Data: " + DDTTProperties.TEST_DATA_FILE_PATH);

		if (!DDTTSupport.propertyValidation()) {
			return;
		}

		AbstractExcelSupport testDataExcelSupport = ExcelSupportFactory.getExcelSupport();
		DatabaseProcessor processor = new DatabaseProcessor(testDataExcelSupport);

		processor.process();

		logger.info("########## Test data is sync successfully! ##########");
	}

	public static void main(String[] args) throws Exception {
		TestDataSynchronizer sync = new TestDataSynchronizer();
		sync.synchronizeData();
	}
}
