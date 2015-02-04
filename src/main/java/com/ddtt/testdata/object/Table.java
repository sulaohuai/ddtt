
package com.ddtt.testdata.object;
 
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
 
public class Table {
         private Set<Table> parents = new HashSet<Table>();
         private List<Table> children = new ArrayList<Table>();
         private String tableName;
         private String dataVersion;
         private String[] primaryKeys;
         private TestDataRecord defaultValue;
         private int deepth;
         private List<Column> columns = new ArrayList<Column>();
         private List<TestDataRecord> records = new ArrayList<TestDataRecord>();
 
         public Column getColumn(String columnName) {
                   if (columnName == null) {
                            return null;
                   }
 
                   for (Column vo : this.columns) {
                            if (columnName.equals(vo.getColumnName())) {
                                     return vo;
                            }
                   }
 
                   return null;
         }
        
         @Override
         public int hashCode() {
                   final int prime = 31;
                   int result = 1;
                  result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
                   return result;
         }
 
         @Override
         public boolean equals(Object obj) {
                   if (this == obj)
                            return true;
                   if (obj == null)
                            return false;
                   if (getClass() != obj.getClass())
                            return false;
                   Table other = (Table) obj;
                   if (tableName == null) {
                            if (other.tableName != null)
                                     return false;
                   } else if (!tableName.equals(other.tableName))
                            return false;
                   return true;
         }
 
 
 
         public void addAllRecords(List<TestDataRecord> records) {
                   this.records.addAll(records);
         }
 
         public List<TestDataRecord> getRecords() {
                   return this.records;
         }
 
         public Table(String tableName) {
                   this.tableName = tableName;
         }
 
         public Column getColumn(int index) {
                   return (this.columns.get(index));
         }
 
         public void addChild(Table vo) {
                   this.children.add(vo);
         }
 
         public void addParent(Table vo) {
                   this.parents.add(vo);
         }
 
         public void addColumn(Column vo) {
                   this.columns.add(vo);
         }
 
         public void addAllColumn(List<Column> columnList) {
                   this.columns.addAll(columnList);
         }
 
         public Set<Table> getParents() {
                   return this.parents;
         }
 
         public List<Table> getChildren() {
                   return this.children;
         }
 
         public List<Column> getColumns() {
                   return this.columns;
         }
 
         public String getTableName() {
                   return this.tableName;
         }
 
         public String getDataVersion() {
                   return this.dataVersion;
         }
 
         public void setDataVersion(String dataVersion) {
                   this.dataVersion = dataVersion;
         }
 
         public String[] getPrimaryKeys() {
                   return this.primaryKeys;
         }
 
         public void setPrimaryKeys(String[] primaryKeys) {
                   this.primaryKeys = primaryKeys;
         }
 
         public TestDataRecord getDefaultValue() {
                   return defaultValue;
         }
 
         public void setDefaultValue(TestDataRecord defaultValue) {
                   this.defaultValue = defaultValue;
         }
 
         public int getDeepth() {
                   return deepth;
         }
 
         public void setDeepth(int deepth) {
                   this.deepth = deepth;
         }
}
 