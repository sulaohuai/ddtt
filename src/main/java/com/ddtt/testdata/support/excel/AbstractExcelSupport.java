package com.ddtt.testdata.support.excel;

import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.ddtt.testdata.support.DDTTProperties;

public abstract class AbstractExcelSupport {
	protected Workbook workbook;
	protected String filePath;
	protected AbstractStyleManager styleManager;

	abstract protected Workbook getWorkbook();

	public boolean accept(String sheetName) throws Exception {
		for (String prefix : DDTTProperties.TABLE_PREFIX) {
			if (sheetName.startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}

	public int getNumberOfSheets() throws Exception {
		return getWorkbook().getNumberOfSheets();
	}

	public void writeToFile() throws Exception {
		FileOutputStream fout = new FileOutputStream(this.filePath);
		getWorkbook().write(fout);
		fout.close();
	}

	public String getCellValue(String sheetName, int row, int column) throws Exception {
		Cell cell = getCell(sheetName, row, column);
		return getCellValueAsString(cell);
	}

	protected Cell getCell(String sheetName, int row, int column) throws Exception {
		Cell c = null;

		Sheet s = getWorkbook().getSheet(sheetName);
		if (row < getNumberOfRows(sheetName)) {
			Row r = s.getRow(row);
			if (r == null) {
				return null;
			}
			if (column < getNumberOfColumns(sheetName, row)) {
				c = r.getCell((short) column);
			}
		}

		return c;
	}

	public int getNumberOfRows(String sheetName) throws Exception {
		int nr = -1;

		Sheet s = getWorkbook().getSheet(sheetName);
		if (s == null) {
			return -1;
		}

		nr = s.getLastRowNum() + 1;

		return nr;
	}

	public short getNumberOfColumns(String sheetName, int row) throws Exception {
		short nc = -1;

		Sheet s = getWorkbook().getSheet(sheetName);
		if (s == null) {
			return -1;
		}

		if (row < getNumberOfRows(sheetName)) {
			Row r = s.getRow(row);
			if (r != null) {
				nc = r.getLastCellNum();
			}
		}
		return nc;
	}

	public boolean isSheetExist(String sheetName) throws Exception {
		try {
			return (getWorkbook().getSheet(sheetName) != null);
		} catch (Exception e) {
			// TODO
		}
		return false;
	}

	public void createSheet(String sheetName) throws Exception {
		getWorkbook().createSheet(sheetName);
	}

	public String getSheetName(int sheetId) {
		try {
			String sheetName = getWorkbook().getSheetName(sheetId);
			return sheetName;
		} catch (Exception e) {
		}
		return null;
	}

	public void setColumnWidth(String sheetName, int columnIndex, int width) throws Exception {
		if (!(isSheetExist(sheetName))) {
			throw new RuntimeException("Sheet " + sheetName + " does not exist!");
		}
		Sheet sheet = getWorkbook().getSheet(sheetName);
		sheet.setColumnWidth(columnIndex, width);
	}

	abstract public void protectSheet(String sheetName, String password) throws Exception;

	public void createCell(String sheetName, int rowIndex, int columnIndex, String value,
			AbstractStyleManager.Style style) throws Exception {
		createCell(sheetName, rowIndex, columnIndex, value, style, null);
	}

	public void createCell(String sheetName, int rowIndex, int columnIndex, String value,
			AbstractStyleManager.Style style, String formula) throws Exception {
		if (!(isSheetExist(sheetName))) {
			if (sheetName.equals(DDTTProperties.TEST_DATA_EXCEL_INDEX_SHEET_NAME)) {
				this.workbook.createSheet(sheetName);
				this.workbook.setSheetOrder(sheetName, 0);
			} else {
				throw new RuntimeException("Sheet " + sheetName + " does not exist!");
			}
		}
		Sheet sheet = getWorkbook().getSheet(sheetName);

		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			row = sheet.createRow(rowIndex);
		}
		if (row == null)
			return;
		try {
			Cell cell = row.getCell(columnIndex);
			if (cell == null) {
				cell = row.createCell(columnIndex);
			}
			cell.setCellValue(value);

			if (style != null) {
				cell.setCellStyle(this.styleManager.getCellStyle(style));
			} else {
				cell.setCellStyle(this.styleManager.getCellStyle(AbstractStyleManager.Style.SIMPLEST_CELL));
			}

			if (formula != null)
				cell.setCellFormula(formula);
		} catch (Exception e) {
			throw new RuntimeException("Error occured at row " + rowIndex + " column " + columnIndex + " in sheet "
					+ sheetName, e);
		}
	}

	protected String getCellValueAsString(Cell cell) {
		if (cell == null) {
			return null;
		}
		int cellType = cell.getCellType();
		String cellValue = null;
		if (cellType == 3) {
			cellValue = null;
		} else if (cellType == 4) {
			cellValue = String.valueOf(cell.getBooleanCellValue());
		} else if (cellType == 5) {
			cellValue = String.valueOf(cell.getErrorCellValue());
		} else if (cellType == 0) {
			cellValue = String.valueOf(cell.getNumericCellValue());
			if (cellValue.contains("."))
				cellValue = cellValue.split("\\.")[0];
		} else if (cellType == 1) {
			cellValue = cell.getStringCellValue();
		} else if (cell.getCachedFormulaResultType() == 3) {
			cellValue = null;
		} else if (cell.getCachedFormulaResultType() == 4) {
			cellValue = String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCachedFormulaResultType() == 5) {
			cellValue = String.valueOf(cell.getErrorCellValue());
		} else if (cell.getCachedFormulaResultType() == 0) {
			cellValue = String.valueOf(cell.getNumericCellValue());
			if (cellValue.contains("."))
				cellValue = cellValue.split("\\.")[0];
		} else if (cell.getCachedFormulaResultType() == 1) {
			cellValue = cell.getStringCellValue();
		}

		return cellValue;
	}

	public void setCellStyle(String sheetName, int rowIndex, int columnIndex, AbstractStyleManager.Style style)
			throws Exception {
		if (!(isSheetExist(sheetName))) {
			throw new RuntimeException("Sheet " + sheetName + " does not exist!");
		}
		Sheet sheet = getWorkbook().getSheet(sheetName);

		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			row = sheet.createRow(rowIndex);
		}
		if (row == null)
			return;
		try {
			Cell cell = row.getCell(columnIndex);
			if (cell == null) {
				cell = row.createCell(columnIndex);
			}

			if (style != null)
				cell.setCellStyle(this.styleManager.getCellStyle(style));
			else
				cell.setCellStyle(this.styleManager.getCellStyle(AbstractStyleManager.Style.SIMPLEST_CELL));
		} catch (Exception e) {
			throw new RuntimeException("Error occured at row " + rowIndex + " column " + columnIndex + " in sheet "
					+ sheetName, e);
		}
	}

	public abstract boolean isStrikeout(String sheetName, int row, int column) throws Exception;
}
