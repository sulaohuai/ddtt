package com.ddtt.testdata.main;

import org.apache.log4j.Logger;

import com.ddtt.testdata.support.DDTTSupport;

public class DDTTExecutor {
	protected static Logger logger = Logger.getLogger(DDTTExecutor.class);

	public static void main(String[] args) throws Exception {
		if (args == null || args.length != 1 
				|| (!"syncTemplate".equals(args[0]) && !"syncData".equals(args[0]) && !"generateERD".equals(args[0]))) {
			logger.error("Please provide the only parameter (1) 'syncTemplate' for synchronizing the template "
					+ "or (2) 'syncData' for synchronizing the test data " +
					"or (3) 'generateERD' for generating the ERD hierarchy.");
		}
		
		if ("syncTemplate".equals(args[0])) {
			TestDataTemplateSynchronizer sync = new TestDataTemplateSynchronizer();
			sync.generateTemplate();
		}
		
		if ("syncData".equals(args[0])) {
			TestDataSynchronizer sync = new TestDataSynchronizer();
			sync.synchronizeData();
		}
		
		if ("generateERD".equals(args[0])) {
			DDTTSupport.getDBMetaDataSupport().printERD();
		}
	}
}
