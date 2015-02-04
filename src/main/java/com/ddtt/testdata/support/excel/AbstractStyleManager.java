package com.ddtt.testdata.support.excel;

import java.util.EnumMap;

import org.apache.poi.ss.usermodel.CellStyle;

public abstract class AbstractStyleManager {
	protected EnumMap<Style, Object> styleMap = new EnumMap<Style, Object>(Style.class);

	public static enum Style {
		SIMPLEST_CELL, TABLE_HEADER, COLUMN_HEADER, COLUMN_PROPERTY, COLUMN_SEPERATOR, DATA_CELL, HYPERLINK, DEFAULTVALUE;
	}

	public abstract CellStyle getCellStyle(Style style);

	protected abstract Object getSimplestCellStyle();

	protected abstract Object getTableHeaderCellStyle();

	protected abstract Object createColumnHeaderCellStyle();

	protected abstract Object getColumnPropertyCellStyle();

	protected abstract Object createColumnSeperatorStyle();

	protected abstract Object createDataCellStyle();

	protected abstract Object createHyperlinkCellStyle();

	protected abstract Object createDefaultValueCellStyle();
	
	protected void init() {
		this.styleMap.put(Style.SIMPLEST_CELL, getSimplestCellStyle());
		this.styleMap.put(Style.TABLE_HEADER, getTableHeaderCellStyle());
		this.styleMap.put(Style.COLUMN_HEADER, createColumnHeaderCellStyle());
		this.styleMap.put(Style.COLUMN_PROPERTY, getColumnPropertyCellStyle());
		this.styleMap.put(Style.COLUMN_SEPERATOR, createColumnSeperatorStyle());
		this.styleMap.put(Style.DATA_CELL, createDataCellStyle());
		this.styleMap.put(Style.HYPERLINK, createHyperlinkCellStyle());
		this.styleMap.put(Style.DEFAULTVALUE, createDefaultValueCellStyle());
	}
}
