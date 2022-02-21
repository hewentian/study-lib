package com.hewentian.util;

import com.hewentian.entity.TwoTuple;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PoiExportDatabaseTables {
    public static void main(String[] args) {
        TwoTuple<Map<String, String>, Map<String, List<String[]>>> tableMetadata = getTableMetadata();
        exportToExcel(tableMetadata.a, tableMetadata.b);
    }

    public static void exportToExcel(Map<String, String> tableNameMap, Map<String, List<String[]>> tableColumnMap) {
        try {
            XSSFWorkbook wb = new XSSFWorkbook();
            int rowIndex = 0;

            XSSFSheet sheet = wb.createSheet("表结构数据");

            sheet.setDefaultColumnWidth(30);
//            sheet.setColumnWidth(3,40);

            //////////////////////////////////////////////////////////////////////////
            XSSFFont font = wb.createFont();
            font.setFontHeight(12);
            font.setColor(IndexedColors.DARK_BLUE.index);
            font.setBold(true);

            XSSFCellStyle cellStyleTitle = wb.createCellStyle();
            cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
            cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyleTitle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
            cellStyleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            cellStyleTitle.setFont(font);
            //////////////////////////////////////////////////////////////////////////
            XSSFRow row;
            XSSFCell cell;

            for (Map.Entry<String, String> entry : tableNameMap.entrySet()) {
                // 生成标题
                row = sheet.createRow(rowIndex);
                row.setHeightInPoints(28);
                cell = row.createCell(0);
                cell.setCellValue(entry.getKey());
                cell.setCellStyle(cellStyleTitle);
                row.createCell(1);
                row.createCell(2);
                row.createCell(3);
                row.createCell(4);
                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 4));

                // 表用途说明
                row = sheet.createRow(++rowIndex);
                row.setHeightInPoints(28);
                cell = row.createCell(0);
                cell.setCellValue("表用途说明");
                cell.setCellStyle(cellStyleTitle);
                cell = row.createCell(1);
                cell.setCellStyle(cellStyleTitle);
                cell.setCellValue(entry.getValue());
                row.createCell(2);
                row.createCell(3);
                row.createCell(4);
                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 1, 4));


                String[] title = new String[]{"字段名", "类型", "长度", "备注", "举例"};
                row = sheet.createRow(++rowIndex);
                row.setHeightInPoints(28);
                for (int i = 0, len = title.length; i < len; i++) {
                    cell = row.createCell(i);
                    cell.setCellValue(title[i]);
                    cell.setCellStyle(cellStyleTitle);
                }

                List<String[]> docs = tableColumnMap.get(entry.getKey());

                // 生成数据行
                for (int i = 0, len = docs.size(); i < len; i++) {
                    row = sheet.createRow(++rowIndex);
                    String[] doc = docs.get(i);

                    int j = 0;
                    for (int lenJ = doc.length; j < lenJ; j++) {
                        cell = row.createCell(j);
                        cell.setCellValue(doc[j]);
                    }
                    cell = row.createCell(j);
                    cell.setCellValue("");
                }

                // 产生2个空行
                sheet.createRow(++rowIndex);
                sheet.createRow(++rowIndex);
                rowIndex++;
            }

            FileOutputStream fos = new FileOutputStream("/home/hewentian/Documents/表结构.xlsx");
            wb.write(fos);
            System.out.println("finished");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TwoTuple<Map<String, String>, Map<String, List<String[]>>> getTableMetadata() {
        Connection conn = null;
        ResultSet rs = null;

        Map<String, String> tableNameMap = new LinkedHashMap<>();
        Map<String, List<String[]>> tableColumnMap = new HashMap<>();

        try {
            conn = JdbcUtil.getConnection();

            DatabaseMetaData dmd = conn.getMetaData();
            System.out.println("数据库名称： " + dmd.getDatabaseProductName());
            System.out.println("数据库版本： " + dmd.getDatabaseProductVersion());
            System.out.println("数据库用户名： " + dmd.getUserName());

            // 读取指定数据库的所有表
            // SELECT TABLE_NAME, TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'test';
            String databaseName = "test";
            rs = dmd.getTables(databaseName, null, null, new String[]{"TABLE"});

            int count = 0;
            System.out.println("All table names are in " + databaseName + " database:");
            while (rs.next()) {
                String tblName = rs.getString("TABLE_NAME");
                String tblRemarks = rs.getString("REMARKS");

                tableNameMap.put(tblName, tblRemarks);

                System.out.println(tblName);
                count++;
            }
            System.out.println(count + " Rows in set ");
            ////////////////////////////////////////////////////////

            // 读取所有表的元数据信息
            for (String tableName : tableNameMap.keySet()) {
                List<String[]> list = new ArrayList<>();

                // 获取指定表的元数据信息
                rs = dmd.getColumns(databaseName, databaseName, tableName, "%");
                while (rs.next()) {
                    String[] data = new String[4];
                    data[0] = rs.getString("COLUMN_NAME");
                    data[1] = rs.getString("TYPE_NAME");
                    data[2] = rs.getString("COLUMN_SIZE");
                    data[3] = rs.getString("REMARKS");

                    list.add(data);
                }

                tableColumnMap.put(tableName, list);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(conn, null, rs);
        }

        return new TwoTuple<>(tableNameMap, tableColumnMap);
    }
}
