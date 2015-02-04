package com.ddtt.testdata.support.excel;

import com.ddtt.testdata.support.DDTTConstants;
import com.ddtt.testdata.support.DDTTProperties;

public class ExcelSupportFactory {

	public static AbstractExcelSupport getExcelSupport() throws Exception {
		if (DDTTConstants.ExcelVersion.EXCEL_2003.equals(DDTTProperties.TEST_DATA_EXCEL_VERSION)) {
			return new Excel2003Support(DDTTProperties.TEST_DATA_FILE_PATH);
		} else if (DDTTConstants.ExcelVersion.EXCEL_2007.equals(DDTTProperties.TEST_DATA_EXCEL_VERSION)) {
			return new Excel2007Support(DDTTProperties.TEST_DATA_FILE_PATH);
		}

		throw new IllegalStateException("Only Excel 2003 and 2007 are supported.");
	}
}
