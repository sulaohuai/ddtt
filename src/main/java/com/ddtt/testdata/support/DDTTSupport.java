package com.ddtt.testdata.support;

import org.apache.log4j.Logger;

import com.ddtt.testdata.base.TestDataService;
import com.ddtt.testdata.util.AppContextUtil;

public class DDTTSupport {
	protected static Logger logger = Logger.getLogger(DDTTSupport.class);

	public static TestDataService getDatabaseSupport() {
		return ((TestDataService) AppContextUtil.getBean("databaseSupport"));
	}

	public static DBMetaDataSupport getDBMetaDataSupport() {
		return (AppContextUtil.getBean("dbMetaDataSupport", DBMetaDataSupport.class));
	}

	public static boolean propertyValidation() {
		if (DDTTProperties.TEST_DATA_EXCEL_VERSION != DDTTConstants.ExcelVersion.EXCEL_2003
				&& DDTTProperties.TEST_DATA_EXCEL_VERSION != DDTTConstants.ExcelVersion.EXCEL_2007) {
			logger.error("ERROR: Only Excel " + DDTTConstants.ExcelVersion.EXCEL_2003 + " and "
					+ DDTTConstants.ExcelVersion.EXCEL_2007 + " are supported.");
			return false;
		}

		if (DDTTProperties.TEST_DATA_EXCEL_VERSION == DDTTConstants.ExcelVersion.EXCEL_2003
				&& DDTTProperties.TEST_DATA_EXCEL_MAX_COLUMN_COUNT > DDTTConstants.ExcelMaxColumnCount.EXCEL_MAX_COLUMN_COUNT_2003) {
			logger.error("ERROR: Max number of columns for " + DDTTConstants.ExcelVersion.EXCEL_2003 + " is "
					+ DDTTConstants.ExcelMaxColumnCount.EXCEL_MAX_COLUMN_COUNT_2003);

			return false;
		}

		if (DDTTProperties.TEST_DATA_EXCEL_VERSION == DDTTConstants.ExcelVersion.EXCEL_2007
				&& DDTTProperties.TEST_DATA_EXCEL_MAX_COLUMN_COUNT > DDTTConstants.ExcelMaxColumnCount.EXCEL_MAX_COLUMN_COUNT_2007) {
			logger.error("ERROR: Max number of columns for " + DDTTConstants.ExcelVersion.EXCEL_2007 + " is "
					+ DDTTConstants.ExcelMaxColumnCount.EXCEL_MAX_COLUMN_COUNT_2007);

			return false;
		}

		return true;
	}
}
