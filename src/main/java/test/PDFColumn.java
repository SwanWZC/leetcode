package test;

import com.alibaba.fastjson.JSONObject;

public class PDFColumn {
    private static final String SQL_TMP1 = "INSERT INTO jhd_print_file_table_info (TABLE_NAME, TABLE_INFO, TABLE_DESC) VALUES ('${tableName}', \"()\", '${tableDesc}');";
    private static final String SQL_TMP2 = "INSERT INTO `jhd_print_file_para_value` (`VALUE_CODE`, `VALUE_DESC`, `TABLE_NAME`, `FIELD_NAME`) VALUES ('${valueCode}', '${valueDesc}', '${tableName}', '${fieldName}');";
    private static final String SQL_TMP3 = "INSERT INTO `jhd_print_file_parameters` (`PARAMETERS_OID`, `FILE_CODE`, `PARA_CODE`, `PARA_NAME`, `VALUE_CODE`,`FORMAT_CODE`) VALUES ('${parametersOid}', '${fileCode}', '${paraCode}', '${paraName}', '${valueCode}',${formatCode});";
    public static void main(String[] args) {
        String json = "{\n" +
                "\"PARAMETERS_OID\":\"612\",\n" +
                "\"FILE_CODE\":\"BIZ_GR_07\",\n" +
                "\"PARA_CODE\":\"K1\",\n" +
                "\"PARA_NAME\":\"上年度全国城镇居民人均可支配收入\",\n" +
                "\"VALUE_CODE\":\"BIZ_INCOME_AVG\",\n" +
                "\"FORMAT_CODE\":\"\",\n" +
                "\"VALUE_DESC\":\"上年度全国城镇居民人均可支配收入\",\n" +
                "\"TABLE_NAME\":\"CFG_PROPERTIES_VALUE_INCOME\",\n" +
                "\"FIELD_NAME\":\"INCOME_AVG\",\n" +
                "\"TABLE_INFO\":\"\",\n" +
                "\"TABLE_DESC\":\"上年度全国城镇居民人均可支配收入\"\n" +
                "}";
        JSONObject jsonObject = JSONObject.parseObject(json);
        String parametersOid = jsonObject.getString("PARAMETERS_OID");
        String fileCode = jsonObject.getString("FILE_CODE");
        String paraCode = jsonObject.getString("PARA_CODE");
        String paraName = jsonObject.getString("PARA_NAME");
        String valueCode = jsonObject.getString("VALUE_CODE");
        String formatCode = jsonObject.getString("FORMAT_CODE").equals("")?"NULL":jsonObject.getString("FORMAT_CODE");
        String valueDesc = jsonObject.getString("VALUE_DESC");
        String tableName = jsonObject.getString("TABLE_NAME");
        String fieldName = jsonObject.getString("FIELD_NAME");
        String tableDesc = jsonObject.getString("TABLE_DESC");
        String sql1 = SQL_TMP1.replace("${fileCode}",fileCode)
                .replace("${parametersOid}",parametersOid)
                .replace("${paraCode}",paraCode)
                .replace("${paraName}",paraName)
                .replace("${valueCode}",valueCode)
                .replace("${formatCode}",formatCode)
                .replace("${valueDesc}",valueDesc)
                .replace("${tableName}",tableName)
                .replace("${fieldName}",fieldName)
                .replace("${tableDesc}",tableDesc);
        String sql2 = SQL_TMP2.replace("${fileCode}",fileCode)
                .replace("${parametersOid}",parametersOid)
                .replace("${paraCode}",paraCode)
                .replace("${paraName}",paraName)
                .replace("${valueCode}",valueCode)
                .replace("${formatCode}",formatCode)
                .replace("${valueDesc}",valueDesc)
                .replace("${tableName}",tableName)
                .replace("${fieldName}",fieldName)
                .replace("${tableDesc}",tableDesc);
        String sql3 = SQL_TMP3.replace("${fileCode}",fileCode)
                .replace("${parametersOid}",parametersOid)
                .replace("${paraCode}",paraCode)
                .replace("${paraName}",paraName)
                .replace("${valueCode}",valueCode)
                .replace("${formatCode}",formatCode)
                .replace("${valueDesc}",valueDesc)
                .replace("${tableName}",tableName)
                .replace("${fieldName}",fieldName)
                .replace("${tableDesc}",tableDesc);
        System.out.println(sql1);
        System.out.println(sql2);
        System.out.println(sql3);
    }
}
