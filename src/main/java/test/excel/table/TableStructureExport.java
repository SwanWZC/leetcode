package test.excel.table;

import com.alibaba.fastjson.JSONArray;
import oracle.sql.CLOB;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class TableStructureExport {
    private static String EXCEL_PATH = "C:\\Users\\86188\\Desktop\\样例数据\\model.xlsx";
    private static String EXCEL_PATH_OUT = "C:\\Users\\86188\\Desktop\\样例数据\\查询统计表结构关联.xlsx";
    private static String URL = "jdbc:oracle:thin:@192.168.0.148:1521:orcl";
    private static String USER = "JSHRSSB_SWHR_DEV";
    private static String PASSWORD = "JSHRSSB_SWHR_DEV";

    public static void main(String[] args) {
        TableStructureExport tableStructureExport = new TableStructureExport();
        XSSFWorkbook wb = tableStructureExport.returnWorkBookGivenFileHandle(EXCEL_PATH);
        XSSFSheet sheet = wb.getSheet("Sheet1");
        int rowNum = sheet.getPhysicalNumberOfRows();
        List<String> tableNameList = new ArrayList<>();
        for (int i = 1; i < rowNum; i++) {
            String tableName = sheet.getRow(i).getCell(0).getStringCellValue();
            if (tableName == null || "".equals(tableName)){
                continue;
            }
            tableNameList.add(tableName);
        }
        tableStructureExport.excelTemplateRead(tableNameList);
    }

    private void excelTemplateRead(List<String> tableNameList){
        XSSFWorkbook wb = returnWorkBookGivenFileHandle(EXCEL_PATH);
        tableNameList.forEach(item->{
            if (item == null || "".equals(item)){
                return;
            }
            List<TableStructureDTO> tableStructureDTOList = getTableStructure(item);
            if (CollectionUtils.isEmpty(tableStructureDTOList)){
                return;
            }
            XSSFSheet sheet = wb.createSheet(item);
            setExcelTitle(sheet);
            setLink(sheet,wb);
            for (int i = 0; i < tableStructureDTOList.size(); i++) {
                TableStructureDTO tableStructureDTO = tableStructureDTOList.get(i);
                Row row = sheet.createRow(i+2);//创建行
                row.createCell(0).setCellValue(tableStructureDTO.getColumnName());
                row.createCell(1).setCellValue(tableStructureDTO.getDataType());
                row.createCell(2).setCellValue(tableStructureDTO.getDataLength());
                row.createCell(3).setCellValue(tableStructureDTO.getNullable());
                row.createCell(4).setCellValue(tableStructureDTO.getComments());
                setCellBorder(i+2,sheet,5);
            }
            int insertIndex = sheet.getPhysicalNumberOfRows();
            Row mainRow = sheet.createRow(insertIndex + 2);//创建行
            mainRow.createCell(0).setCellValue("样例数据");
            int index = insertIndex + 3;
            List<JSONArray> tableDataList = getTableData(item);
            for (JSONArray jsonArray: tableDataList) {
                Row row = sheet.createRow(index);//创建行
                for (int i = 0; i < jsonArray.size(); i++) {
                    row.createCell(i).setCellValue(jsonArray.get(i)==null?"":jsonArray.get(i).toString());
                    setCellBorder(index,sheet,jsonArray.size());
                }
                index++;
            }
            int maxColumnNum = Math.max(5,tableDataList.get(0).size());
            for (int i = 0; i < maxColumnNum; i++) {
                sheet.autoSizeColumn(i);
            }
        });
        int sheetIndex = wb.getSheetIndex("Sheet1");
        wb.removeSheetAt(sheetIndex);
        saveExcel(wb,EXCEL_PATH_OUT);
    }

    private void setLink(XSSFSheet sheet, XSSFWorkbook wb){
        XSSFRow row = sheet.getRow(0);
        XSSFCell linkCell = row.createCell((short)0);
        CreationHelper createHelper = wb.getCreationHelper();
        XSSFHyperlink  link = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.URL);
        link.setAddress("#目录!A1");
        linkCell.setHyperlink(link);
        linkCell.setCellValue("返回");
        XSSFCellStyle linkStyle = wb.createCellStyle();
        XSSFFont cellFont= wb.createFont();
        cellFont.setUnderline((byte) 1);
        cellFont.setColor(HSSFColor.BLUE.index);
        linkStyle.setFont(cellFont);
        linkCell.setCellStyle(linkStyle);
    }

    private void setExcelTitle(Sheet sheet){
        Row row = sheet.createRow(1);//创建行
        row.createCell(0).setCellValue("字段名称");
        row.createCell(1).setCellValue("字段类型");
        row.createCell(2).setCellValue("字段长度");
        row.createCell(3).setCellValue("是否可为空");
        row.createCell(4).setCellValue("备注");
        row.setHeightInPoints(21);
        setCellBorder(0,sheet,5);
    }
    private void setCellBorder(int rowNum, Sheet sheet,int columnNum) {
        for (int i = 0; i < columnNum; i++) {
            CellRangeAddress regionBorder = new CellRangeAddress(rowNum, rowNum, 0, i);
            RegionUtil.setBorderBottom(BorderStyle.THIN, regionBorder, sheet);
            RegionUtil.setBorderLeft(BorderStyle.THIN, regionBorder, sheet);
            RegionUtil.setBorderRight(BorderStyle.THIN, regionBorder, sheet);
            RegionUtil.setBorderTop(BorderStyle.THIN, regionBorder, sheet);
        }
    }
    private List<TableStructureDTO> getTableStructure(String tableName){
        System.out.println("tableName is :"+tableName);
        Connection conn = JDBCConnectionFactory.getConnection(URL,USER,PASSWORD);
        String tableStructureSql = "select " +
                "      ut.COLUMN_NAME, " +
                "      ut.DATA_TYPE, " +
                "      ut.DATA_LENGTH, " +
                "      ut.NULLABLE, " +
                "      uc.COMMENTS " +
                "from user_tab_columns  ut " +
                "inner JOIN user_col_comments uc " +
                "on ut.TABLE_NAME  = uc.table_name and ut.COLUMN_NAME = uc.column_name " +
                "where ut.Table_Name= ? order by ut.COLUMN_ID";


        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<TableStructureDTO> tableStructureDTOList = new ArrayList<>();
        try {
            stmt = conn.prepareStatement(tableStructureSql);
            stmt.setString(1,tableName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                TableStructureDTO tableStructureDTO = new TableStructureDTO();
                tableStructureDTO.setColumnName(rs.getString("COLUMN_NAME"));
                tableStructureDTO.setDataType(rs.getString("DATA_TYPE"));
                tableStructureDTO.setDataLength(rs.getString("DATA_LENGTH"));
                tableStructureDTO.setNullable(rs.getString("NULLABLE"));
                tableStructureDTO.setComments(rs.getString("COMMENTS"));
                tableStructureDTOList.add(tableStructureDTO);
            }
        } catch (SQLException e) {
            JDBCConnectionFactory.close(conn,stmt,rs);
            e.printStackTrace();
        }
        return tableStructureDTOList;
    }

    private List<JSONArray> getTableData(String tableName){
        System.out.println("GetTableData tableName is :"+tableName);
        Connection conn = JDBCConnectionFactory.getConnection(URL,USER,PASSWORD);
        String tableStructureSql = "SELECT table_alias.* FROM (SELECT ROWNUM AS rowNo, t.* FROM ${tableName} t) table_alias WHERE table_alias.rowNo <= 100";
        tableStructureSql = tableStructureSql.replace("${tableName}",tableName);
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<JSONArray> dataList = new ArrayList<>();
        try {
            stmt = conn.prepareStatement(tableStructureSql);
            rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnNum = rs.getMetaData().getColumnCount();
            JSONArray tableTitle = new JSONArray();
            for (int i = 2; i <= columnNum; i++) {
                tableTitle.add(rsmd.getColumnName(i));
            }
            dataList.add(tableTitle);
            while (rs.next()) {
                JSONArray jsonArray = new JSONArray();
                for (int i = 2; i <= columnNum; i++) {
                    if ("CLOB".equals(rsmd.getColumnTypeName(i))){
                        jsonArray.add(clobToString((oracle.sql.CLOB)rs.getClob(i)));
                    }else {
                        jsonArray.add(rs.getObject(i));
                    }

                }
                dataList.add(jsonArray);
            }
        } catch (SQLException | IOException e) {
            JDBCConnectionFactory.close(conn,stmt,rs);
            e.printStackTrace();
        }

        return dataList;
    }

    // 将字CLOB转成STRING类型
    public String clobToString(CLOB clob) throws SQLException, IOException {
        if (clob == null){
            return "";
        }
        Reader is = clob.getCharacterStream();// 得到流
        BufferedReader br = new BufferedReader(is);
        String s = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
            sb.append(s);
            s = br.readLine();
        }
        is.close();
        br.close();
        return sb.toString();
    }

    private  XSSFWorkbook returnWorkBookGivenFileHandle(String excelPtah) {
        XSSFWorkbook wb = null;
        FileInputStream fis = null;
        File f = new File(excelPtah);
        try {
            if (f != null) {
                fis = new FileInputStream(f);
                wb = new XSSFWorkbook(fis);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return wb;
    }
    private void saveExcel(XSSFWorkbook wb, String excelOutPath) {
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(excelOutPath);
            wb.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
