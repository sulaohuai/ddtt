package com.ddtt.testdata.base;

import java.util.List;
import java.util.Set;

import com.ddtt.testdata.object.Table;

public abstract interface TestDataService {
	public abstract void processTestData(List<Table> sortedTableList, String paramString) throws Exception;
	
	public void executeSqlFile(String fileName) throws Exception;
	
	public abstract void disableTriggers(Set<String> paramSet) throws Exception;

	public abstract void enableTriggers(Set<String> paramSet) throws Exception;
}
