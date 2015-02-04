package com.ddtt.testdata.support.excel;

import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;

public class Excel2003Support extends AbstractExcelSupport {

	public Excel2003Support(String filePath) throws Exception {
		this.filePath = filePath;

		FileInputStream is = null;
		try {
			is = new FileInputStream(filePath);
			POIFSFileSystem ps = new POIFSFileSystem(is);
			this.workbook = new HSSFWorkbook(ps);

			this.styleManager = new Excel2003StyleManager(getWorkbook());
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	@Override
	protected HSSFWorkbook getWorkbook() {
		return (HSSFWorkbook) this.workbook;
	}

	@Override
	public boolean isStrikeout(String sheetName, int row, int column) throws Exception {
		HSSFCell fieldNameCell = (HSSFCell) getCell(sheetName, row, column);
		if (fieldNameCell == null) {
			return false;
		}
		boolean isStrikeout = fieldNameCell.getCellStyle().getFont(this.workbook).getStrikeout();
		return isStrikeout;
	}

	@Override
	public void protectSheet(String sheetName, String password) throws Exception {
		if (!(isSheetExist(sheetName))) {
			throw new RuntimeException("Sheet " + sheetName + " does not exist!");
		}
		Sheet sheet = getWorkbook().getSheet(sheetName);
		sheet.protectSheet(password);
	}
}
