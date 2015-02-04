package com.ddtt.testdata.main;

import org.apache.log4j.Logger;

import com.ddtt.testdata.support.DDTTSupport;

public class ERDGenerator {
	private static Logger logger = Logger.getLogger(ERDGenerator.class);

	public static void main(String[] args) throws Exception {
		logger.info("########### Start to print ERD ###########");
		DDTTSupport.getDBMetaDataSupport().printERD();
		logger.info("########### Print ERD finished ###########");
	}
}
