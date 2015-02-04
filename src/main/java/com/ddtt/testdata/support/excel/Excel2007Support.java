package com.ddtt.testdata.support.excel;

import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetProtection;

public class Excel2007Support extends AbstractExcelSupport {

	public Excel2007Support(String filePath) throws Exception {
		this.filePath = filePath;

		FileInputStream is = null;
		try {
			is = new FileInputStream(filePath);
			XSSFWorkbook newWorkbook = new XSSFWorkbook(is);
			this.workbook = newWorkbook;

			this.styleManager = new Excel2007StyleManager(newWorkbook);
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	@Override
	protected XSSFWorkbook getWorkbook() {
		return (XSSFWorkbook) this.workbook;
	}

	@Override
	public boolean isStrikeout(String sheetName, int row, int column) throws Exception {
		XSSFCell fieldNameCell = (XSSFCell) getCell(sheetName, row, column);
		if (fieldNameCell == null) {
			return false;
		}
		boolean isStrikeout = fieldNameCell.getCellStyle().getFont().getStrikeout();
		return isStrikeout;
	}

	@Override
	public void protectSheet(String sheetName, String password) throws Exception {
		if (!(isSheetExist(sheetName))) {
			throw new RuntimeException("Sheet " + sheetName + " does not exist!");
		}
		XSSFSheet sheet = getWorkbook().getSheet(sheetName);

		sheet.enableLocking();
		CTSheetProtection sheetProtection = sheet.getCTWorksheet().getSheetProtection();
		sheetProtection.setSelectLockedCells(true);
		sheetProtection.setSelectUnlockedCells(false);
		sheetProtection.setFormatCells(true);
		sheetProtection.setFormatColumns(true);
		sheetProtection.setFormatRows(true);
		sheetProtection.setInsertColumns(true);
		sheetProtection.setInsertRows(true);
		sheetProtection.setInsertHyperlinks(true);
		sheetProtection.setDeleteColumns(true);
		sheetProtection.setDeleteRows(true);
		sheetProtection.setSort(true);
		sheetProtection.setAutoFilter(true);
		sheetProtection.setPivotTables(true);
		sheetProtection.setObjects(true);
		sheetProtection.setScenarios(true);
	}
}
