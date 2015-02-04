
package com.ddtt.testdata.process;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ddtt.testdata.object.Table;
import com.ddtt.testdata.object.TestDataRecord;
import com.ddtt.testdata.support.DDTTProperties;
import com.ddtt.testdata.support.DDTTSupport;
import com.ddtt.testdata.support.excel.AbstractExcelSupport;
 
public class DatabaseProcessor {
         protected Logger logger = Logger.getLogger(this.getClass());
 
         private ExcelReader testDataExcelReader;
 
         public DatabaseProcessor(AbstractExcelSupport testDataExcelSupport) {
                   this.testDataExcelReader = new ExcelReader(testDataExcelSupport);
         }
 
         public void process() throws Exception {
                   Map<String, Table> m = new HashMap<String, Table>();
 
                   List<String> tableNameList = this.testDataExcelReader.getTableNameList();
 
                   logger.info("=========== Start to get records ==========");
                   for (String tableName : tableNameList) {
                            boolean isListedTables = isInListedTable(tableName);
                            boolean isChildOfListedTables = false;
                           
                            if (DDTTProperties.TEST_DATA_TAKE_CARE_OF_CHILD_TABLE) {
                                     isChildOfListedTables = isChildOfListedTables(tableName);
                            }
                           
                            if (isListedTables || isChildOfListedTables) {
                                     // If current table is a child table of the listed table, no need to
                                     // provide the same version to child table, because all test data of
                                     // the child table need to be deleted before delete the data for
                                     // parent table
                                     String version = null;
                                     if (isListedTables){
                                               version = getVersion(tableName);
                                     }
        
                                     List<TestDataRecord> recordList = this.testDataExcelReader.getRecordList(tableName, version);
        
                                     Table table = this.testDataExcelReader.getTable(tableName);
                                     table.addAllRecords(recordList);
        
                                     TestDataRecord defaultValue = this.testDataExcelReader.getDefaultValue(tableName);
                                     table.setDefaultValue(defaultValue);
        
                                     m.put(tableName, table);
                            }
                   }
                   logger.info("=========== Get records successfully ==========");
                   
                   logger.info("=========== Start to sort tables ==========");
                   List<Table> sortedTableListFull = DDTTSupport.getDBMetaDataSupport().getAscSortedTableListByDeepth();
                   List<Table> sortedTableList = new ArrayList<Table>();
                   for(Table t : sortedTableListFull){
                            if(m.get(t.getTableName()) != null){
                                     sortedTableList.add(m.get(t.getTableName()));
                            }
                   }
                   logger.info("=========== Sort tables successfully ==========");
 
                   DDTTSupport.getDatabaseSupport().disableTriggers(m.keySet());
                   try {
                	   DDTTSupport.getDatabaseSupport().executeSqlFile(DDTTProperties.TEST_DATA_SQL_BEFORE);
                       DDTTSupport.getDatabaseSupport().processTestData(sortedTableList, DDTTProperties.TEST_DATA_PROCESS_TYPE);
                	   DDTTSupport.getDatabaseSupport().executeSqlFile(DDTTProperties.TEST_DATA_SQL_AFTER);
                   } finally {
                       DDTTSupport.getDatabaseSupport().enableTriggers(m.keySet());
                   }
         }
 
         private boolean isInListedTable(String tableName) throws Exception {
                   if ((DDTTProperties.TEST_DATA_PROCESS_LISTED_TABLES == null)
                                     || (DDTTProperties.TEST_DATA_PROCESS_LISTED_TABLES.length == 0)) {
                            return true;
                   }
 
                   for (Table table : DDTTProperties.TEST_DATA_PROCESS_LISTED_TABLES) {
                            if (tableName.equalsIgnoreCase(table.getTableName())) {
                                     return true;
                            }
                   }
 
                   return false;
         }
 
         private boolean isChildOfListedTables(String tableName) throws Exception {
                   if(DDTTProperties.TEST_DATA_PROCESS_LISTED_TABLES == null){
                            return false;
                   }
                  
                   for (Table table : DDTTProperties.TEST_DATA_PROCESS_LISTED_TABLES) {
                            boolean isChildTable = DDTTSupport.getDBMetaDataSupport().isChildOf(tableName, table.getTableName());
                            if (isChildTable) {
                                     return true;
                            }
                   }
 
                   return false;
         }
 
         private String getVersionOfParentTable(String tableName) throws Exception {
                   if (DDTTProperties.TEST_DATA_TAKE_CARE_OF_CHILD_TABLE) {
                            for (Table table : DDTTProperties.TEST_DATA_PROCESS_LISTED_TABLES) {
                                     boolean isChildTable = DDTTSupport.getDBMetaDataSupport().isChildOf(tableName, table.getTableName());
                                     if (isChildTable) {
                                               return getVersion(table.getTableName());
                                     }
                            }
                   }
 
                   return null;
         }
 
         private String getVersion(String tableName) {
                   if (DDTTProperties.TEST_DATA_PROCESS_LISTED_TABLES == null) {
                            return null;
                   }
 
                   for (Table vo : DDTTProperties.TEST_DATA_PROCESS_LISTED_TABLES) {
                            if (tableName.equals(vo.getTableName())) {
                                     return vo.getDataVersion();
                            }
                   }
 
                   return null;
         }
}
 
 