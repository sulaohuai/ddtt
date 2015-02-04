package com.ddtt.testdata.support.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

public class Excel2003StyleManager extends AbstractStyleManager {
	HSSFWorkbook workbook;

	public Excel2003StyleManager(HSSFWorkbook workbook) {
		this.workbook = workbook;
		init();
	}

	@Override
	public HSSFCellStyle getCellStyle(Style style) {
		return ((HSSFCellStyle) this.styleMap.get(style));
	}

	@Override
	protected HSSFCellStyle getSimplestCellStyle() {
		return this.workbook.createCellStyle();
	}

	@Override
	protected HSSFCellStyle getTableHeaderCellStyle() {
		HSSFCellStyle style = this.workbook.createCellStyle();

		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setAlignment(CellStyle.ALIGN_LEFT);

		style.setWrapText(true);

		HSSFFont font = this.workbook.createFont();
		font.setFontHeightInPoints((short) 8);
		font.setBoldweight((short) 700);

		style.setFont(font);

		return style;
	}

	@Override
	protected HSSFCellStyle getColumnPropertyCellStyle() {
		HSSFCellStyle style = this.workbook.createCellStyle();

		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setAlignment(CellStyle.ALIGN_LEFT);

		HSSFFont font = this.workbook.createFont();
		font.setFontHeightInPoints((short) 8);

		style.setFont(font);

		style.setWrapText(true);

		return style;
	}

	@Override
	protected HSSFCellStyle createColumnHeaderCellStyle() {
		HSSFCellStyle style = this.workbook.createCellStyle();

		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setAlignment(CellStyle.ALIGN_LEFT);

		style.setFillForegroundColor((short) 40);
		style.setFillPattern((short) 1);

		style.setWrapText(true);

		HSSFFont font = this.workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setBoldweight((short) 700);

		style.setFont(font);

		return style;
	}

	@Override
	protected HSSFCellStyle createColumnSeperatorStyle() {
		HSSFCellStyle style = this.workbook.createCellStyle();
		style.setFillForegroundColor((short) 13);
		style.setFillPattern((short) 1);

		style.setWrapText(true);

		return style;
	}

	@Override
	protected HSSFCellStyle createDataCellStyle() {
		HSSFCellStyle style = this.workbook.createCellStyle();

		style.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
		style.setAlignment(CellStyle.ALIGN_LEFT);

		style.setWrapText(false);

		style.setLocked(false);

		HSSFFont font = this.workbook.createFont();
		font.setFontHeightInPoints((short) 8);

		style.setFont(font);

		return style;
	}

	@Override
	protected HSSFCellStyle createDefaultValueCellStyle() {
		HSSFCellStyle style = this.workbook.createCellStyle();

		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setAlignment(CellStyle.ALIGN_LEFT);

		style.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
		style.setAlignment(CellStyle.ALIGN_LEFT);
		
		HSSFFont font = this.workbook.createFont();
		font.setFontHeightInPoints((short) 8);

		style.setFont(font);

		style.setWrapText(false);

		return style;
	}

	@Override
	protected HSSFCellStyle createHyperlinkCellStyle() {
		HSSFCellStyle style = this.workbook.createCellStyle();

		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setAlignment(CellStyle.ALIGN_LEFT);

		style.setWrapText(true);

		HSSFFont font = this.workbook.createFont();
		font.setFontHeightInPoints((short) 8);
		font.setUnderline(Font.U_NONE);

		style.setFont(font);

		return style;
	}
}
