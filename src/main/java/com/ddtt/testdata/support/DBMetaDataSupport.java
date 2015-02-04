package com.ddtt.testdata.support;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.ddtt.testdata.base.Filter;
import com.ddtt.testdata.object.Column;
import com.ddtt.testdata.object.Table;
import com.ddtt.testdata.util.StringUtil;

public class DBMetaDataSupport {
    protected Logger logger = Logger.getLogger(this.getClass());

    private static String DEFAULT_TABLE_PATTERN = "%";
    private static String IDENT_CHAR = "\t";
    private List<Table> erdTableList;
    private DatabaseMetaData metaData;
    private DataSource dataSource;

    public List<String> getTableNameList() throws SQLException {
              return getTableNameList(null);
    }

    public List<String> getTableNameList(Filter filter) throws SQLException {
              String[] tablePrefixes = DDTTProperties.TABLE_PREFIX;
              if (tablePrefixes == null) {
                       return getTableNameList(DEFAULT_TABLE_PATTERN, filter);
              }

              List<String> tableNameList = new ArrayList<String>();
              for (String prefix : tablePrefixes) {
                       if (StringUtil.isNotEmpty(prefix)) {
                               tableNameList.addAll(getTableNameList(new StringBuilder().append(prefix).append(DEFAULT_TABLE_PATTERN)
                                                   .toString(), filter));
                       }
              }

              return tableNameList;
    }

    public List<String> getTableNameList(String tablePattern, Filter filter) throws SQLException {
              List<String> tableList = new ArrayList<String>();
              ;
              ResultSet tableRs = null;
              try {
                       // TODO to define the catalog
                       tableRs = this.metaData.getTables(null, DDTTProperties.DB_SCHEMA, tablePattern, new String[] { "TABLE" });

                       while (tableRs.next()) {
                                String tableName = tableRs.getString("TABLE_NAME");
                                if ((filter != null) && (filter.filtered(tableName))) {
                                          continue;
                                }
                                tableList.add(tableName);
                       }
              } finally {
                       if (tableRs != null) {
                                tableRs.close();
                       }
              }

              return tableList;
    }

    public String[] getPKs(String table) throws SQLException {
              String[] pks = null;
              ResultSet pkRs = null;
              try {
                       pkRs = this.metaData.getPrimaryKeys(null, DDTTProperties.DB_SCHEMA, table);

                       List<String> pkList = new ArrayList<String>();
                       while (pkRs.next()) {
                                pkList.add(pkRs.getString("COLUMN_NAME"));
                       }

                       if (pkList.size() > 0) {
                                pks = new String[pkList.size()];
                                System.arraycopy(pkList.toArray(), 0, pks, 0, pkList.size());
                       }
              } finally {
                       if (pkRs != null) {
                                pkRs.close();
                       }
              }
              return pks;
    }

    public List<Column> getColumnList(String table) throws SQLException {
              List<Column> columnList = null;
              ResultSet columnRs = null;

              try {
                       columnRs = this.metaData.getColumns(null, DDTTProperties.DB_SCHEMA, table, null);
                       columnList = new ArrayList<Column>();
                       while (columnRs.next()) {
                                Column column = new Column(columnRs.getString("COLUMN_NAME"));
                                Integer dataType = Integer.valueOf(columnRs.getInt("DATA_TYPE"));
                                String typeName = columnRs.getString("TYPE_NAME");
                                Integer length = Integer.valueOf(columnRs.getInt("COLUMN_SIZE"));
                                Boolean nullable = Boolean.valueOf(columnRs.getBoolean("NULLABLE"));
                                String comments = columnRs.getString("REMARKS");
                                Integer precision = Integer.valueOf(columnRs.getInt("COLUMN_SIZE"));
                                Integer scale = Integer.valueOf(columnRs.getInt("DECIMAL_DIGITS"));
                                Object defaultValue = columnRs.getObject("COLUMN_DEF");
                                column.setDataType(dataType);
                                column.setTypeName(typeName);
                                column.setLength(length);
                                column.setNullable(nullable);
                                column.setComments(comments);
                                column.setPrecision(precision);
                                 column.setScale(scale);
                                column.setDefaultValue(defaultValue);
                                columnList.add(column);
                       }
              } finally {
                       if (columnRs != null) {
                                columnRs.close();
                       }
              }

              return columnList;
    }

    public List<Table> getTableList(Filter filter) throws SQLException {
              logger.info("=========== Start to load tables ... ===========");
              List<Table> tableNameList = new ArrayList<Table>();
              List<String> tableList = getTableNameList(filter);
              int i = 0;
              int size = tableList.size();

              for (String tableName : tableList) {
                       logger.info(new StringBuilder().append("Load meta data for table: ").append(tableName).append(" [")
                                          .append(++i).append("/").append(size).append("]").toString());
                       Table table = populateTable(tableName);
                       tableNameList.add(table);
              }
              logger.info("=========== End to load tables ===========");

              return tableNameList;
    }

    private Table populateTable(String tableName) throws SQLException {
              Table table = new Table(tableName);
              String[] pks = getPKs(tableName);
              List<Column> columnList = getColumnList(tableName);
              table.setPrimaryKeys(pks);
              table.addAllColumn(columnList);
              return table;
    }

    public List<String> getChildTableNameList(String tableName) throws SQLException {
              List<String> childTableList = new ArrayList<String>();
              ResultSet childTableRs = null;

              try {
                       //logger.info("Table: " + tableName);
                       childTableRs = this.metaData.getExportedKeys(null, DDTTProperties.DB_SCHEMA, tableName);
                       while (childTableRs.next()) {
                                String childTable = childTableRs.getString("FKTABLE_NAME");
                                if(!childTableList.contains(childTable)){ // avoid multiple foreign key for the same table
                                          childTableList.add(childTable);
                                }
                       }
              } finally {
                       if (childTableRs != null) {
                                childTableRs.close();
                       }
              }

              return childTableList;
    }

    /**
    * Return table list with hierarchy
    */
    public List<Table> getTableListWithHierarchy() throws SQLException {
              if (this.erdTableList == null){
                       Timer timer = new Timer();
                       timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                          logger.info("Get table hierarchy from database ...");
                                }
                       }, 3000, 3000);

                       this.erdTableList = getTableListWithHierarchyFromDatabase();
                      
                       timer.cancel();
              }
              return this.erdTableList;
    }
   
    /**
    * Return table list without hierarchy, but with deepth and children
    */
    private List<Table> getTableListWithDeepthAndChildren() throws SQLException{
              List<Table> tableList = getTableListWithHierarchy();
              Map<String, Table> tableMap = new HashMap<String, Table>();
             
              for(Table t : tableList){
                       calculateDeepth(t, 1, tableMap);
              }
             
              List<Table> result = new ArrayList<Table>();
              result.addAll(tableMap.values());
             
              return result;
    }

    /**
    * Return sorted table list without hierarchy, but with deepth and children
    */
    public List<Table> getAscSortedTableListByDeepth() throws SQLException{
              List<Table> result = getTableListWithDeepthAndChildren();
              Collections.sort(result, new Comparator<Table>(){
                       public int compare(Table t1, Table t2){
                                if(t1.getDeepth() == t2.getDeepth()){
                                          return t1.getTableName().compareTo(t2.getTableName());
                                }
                               
                                return t1.getDeepth() > t2.getDeepth() ? 1 : -1;
                       }
              });
             
//          for(Table t : result){
//                    logger.info("Table Name: " + t.getTableName() + ", Deepth: " + t.getDeepth());
//          }
             
              return result;
    }
   
    private void calculateDeepth(Table t, int deepth, Map<String, Table> result){
              Table current = result.get(t.getTableName());
             
              if(current == null){
                       t.setDeepth(deepth);
                       result.put(t.getTableName(), t);
              } else {
                       if(current.getDeepth() < deepth){
                                current.setDeepth(deepth);
                       }
              }
             
              if(t.getChildren() != null && t.getChildren().size() > 0){
                       for(Table child : t.getChildren()){
                                calculateDeepth(child, deepth+1, result);
                       }
              }
    }
   
    /**
    * Return table list with hierarchy
    */
    private List<Table> getTableListWithHierarchyFromDatabase() throws SQLException {
              Table table;
              List<String> tableNameList = getTableNameList();
              List<Table> tableList = new ArrayList<Table>();

              for (String tableName : tableNameList) {
                       table = find(tableList, tableName);
                       if (table == null) {
                                table = new Table(tableName);
                                tableList.add(table);
                       }
                       List<String> childTableNameList = getChildTableNameList(tableName);
                       for (String fkTable : childTableNameList) {
                                if (!(tableName.equals(fkTable))) {
                                          Table voChild = find(tableList, fkTable);
                                          if (voChild == null) {
                                                   voChild = new Table(fkTable);
                                          } else {
                                                   tableList.remove(voChild);
                                          }
                                          voChild.addParent(table);
                                          table.addChild(voChild);
                                }
                       }
              }
              return tableList;
    }

    public Table find(String tableName) throws SQLException {
              return find(getTableListWithHierarchy(), tableName);
    }

    public Table find(List<Table> voList, String table) {
              if (voList == null) {
                       return null;
              }

              for (Table vo : voList) {
                       if (vo.getTableName().equals(table)) {
                                return vo;
                       }
                       Table temp = find(vo.getChildren(), table);
                       if (temp != null) {
                                return temp;
                       }
              }
              return null;
    }

    private void printERD(List<Table> list, String prefix) {
              if (list == null)
                       return;
              if (prefix == null)
                       prefix = "";

              for (Table vo : list) {
                       logger.info(new StringBuilder().append(prefix).append((prefix.contains(IDENT_CHAR)) ? "|-" : "")
                                          .append(vo.getTableName()).toString());
                       printERD(vo.getChildren(), new StringBuilder().append(prefix).append(IDENT_CHAR).toString());
              }
    }

    public void printERD() throws Exception {
              logger.info("########### Start to print ERD ###########");

              List<Table> tableList = getTableListWithHierarchy();
             
              printERD(tableList, "ERD ==> ");
              logger.info("########### Print ERD finished ###########");
    }

    public boolean isChildOf(String thisTableName, String tableName) throws Exception {
              Table vo = find(tableName);

              if (vo == null) {
                       return false;
              }
              for (Table child : vo.getChildren()) {
                       if (thisTableName.equalsIgnoreCase(child.getTableName())) {
                                return true;
                       }

                       boolean isChild = isChildOf(thisTableName, child.getTableName());
                       if (isChild) {
                                return true;
                       }
              }
              return false;
    }

    public List<String> getSchemaList() throws SQLException {
              List<String> schemaList = null;
              ResultSet schemaRes = null;
              try {
                       schemaRes = this.metaData.getSchemas();
                       schemaList = new ArrayList<String>();
                       while (schemaRes.next()) {
                                String schema = schemaRes.getString("TABLE_SCHEM");
                                schemaList.add(schema);
                       }
              } finally {
                       if (schemaRes != null) {
                                schemaRes.close();
                       }
              }
              return schemaList;
    }

    public DatabaseMetaData getMetaData() {
              return this.metaData;
    }

    public DataSource getDataSource() {
              return this.dataSource;
    }

    public void setDataSource(DataSource dataSource) throws SQLException {
              this.dataSource = dataSource;
              this.metaData = dataSource.getConnection().getMetaData();
    }
}

