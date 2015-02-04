package com.ddtt.testdata.support.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel2007StyleManager extends AbstractStyleManager {
	XSSFWorkbook workbook;

	public Excel2007StyleManager(XSSFWorkbook workbook) {
		this.workbook = workbook;
		init();
	}

	@Override
	public XSSFCellStyle getCellStyle(Style style) {
		return ((XSSFCellStyle) styleMap.get(style));
	}

	@Override
	protected XSSFCellStyle getSimplestCellStyle() {
		return this.workbook.createCellStyle();
	}

	@Override
	protected XSSFCellStyle getTableHeaderCellStyle() {
		XSSFCellStyle style = this.workbook.createCellStyle();

		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setAlignment(CellStyle.ALIGN_LEFT);

		style.setWrapText(true);

		XSSFFont font = this.workbook.createFont();
		font.setFontHeightInPoints((short) 8);
		font.setBoldweight((short) 700);

		style.setFont(font);

		return style;
	}

	@Override
	protected XSSFCellStyle getColumnPropertyCellStyle() {
		XSSFCellStyle style = this.workbook.createCellStyle();

		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setAlignment(CellStyle.ALIGN_LEFT);

		XSSFFont font = this.workbook.createFont();
		font.setFontHeightInPoints((short) 8);

		style.setFont(font);

		style.setWrapText(true);

		return style;
	}

	@Override
	protected XSSFCellStyle createColumnHeaderCellStyle() {
		XSSFCellStyle style = this.workbook.createCellStyle();

		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setAlignment(CellStyle.ALIGN_LEFT);

		style.setFillForegroundColor((short) 40);
		style.setFillPattern((short) 1);

		style.setWrapText(true);

		XSSFFont font = this.workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setBoldweight((short) 700);

		style.setFont(font);

		return style;
	}

	@Override
	protected XSSFCellStyle createColumnSeperatorStyle() {
		XSSFCellStyle style = this.workbook.createCellStyle();
		style.setFillForegroundColor((short) 13);
		style.setFillPattern((short) 1);

		style.setWrapText(true);

		return style;
	}

	@Override
	protected XSSFCellStyle createDataCellStyle() {
		XSSFCellStyle style = this.workbook.createCellStyle();

		XSSFDataFormat dataFormat = this.workbook.createDataFormat();
		style.setDataFormat(dataFormat.getFormat("text"));
		style.setAlignment(CellStyle.ALIGN_LEFT);

		style.setWrapText(false);

		style.setLocked(false);

		XSSFFont font = this.workbook.createFont();
		font.setFontHeightInPoints((short) 8);

		style.setFont(font);

		return style;
	}

	@Override
	protected XSSFCellStyle createDefaultValueCellStyle() {
		XSSFCellStyle style = this.workbook.createCellStyle();
		
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setAlignment(CellStyle.ALIGN_LEFT);

		XSSFDataFormat dataFormat = this.workbook.createDataFormat();
		style.setDataFormat(dataFormat.getFormat("text"));
		style.setAlignment(CellStyle.ALIGN_LEFT);
		
		XSSFFont font = this.workbook.createFont();
		font.setFontHeightInPoints((short) 8);

		style.setFont(font);

		style.setWrapText(false);

		style.setLocked(false);
		
		return style;
	}

	@Override
	protected XSSFCellStyle createHyperlinkCellStyle() {
		XSSFCellStyle style = this.workbook.createCellStyle();

		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setAlignment(CellStyle.ALIGN_LEFT);

		style.setWrapText(true);

		XSSFFont font = this.workbook.createFont();
		font.setFontHeightInPoints((short) 8);
		font.setUnderline(Font.U_NONE);

		style.setFont(font);

		return style;
	}
}
