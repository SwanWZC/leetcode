package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    /**
     *  组装信息
     * @param soap 原始信息
     * @param map 根据原始信息占位符中的内容作为key,获取替换进占位符中的内容
     * @return
     */
    public static String getMes(String soap, Map<String, Object> map) {
        String startStr = "${";
        String endStr = "}";
        List<String> subUtil = getSubUtil(soap);
        for (String s : subUtil) {
            if (map.containsKey(s) && null != map.get(s)) {
                soap = soap.replace(startStr + s + endStr, map.get(s).toString());
            }
        }
        return soap;
    }

    private static List<String> getSubUtil(String soap) {
        String regex = "\\$\\{(.*?)\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(soap);
        List<String> list = new ArrayList<String>();
        int i = 1;
        while (matcher.find()) {
            list.add(matcher.group(i));
        }
        return list;
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "jake");
        map.put("age", 18);
        map.put("work", "学生");
        map.put("color", "");
        String mes = getMes("我的朋友是${name},他今年${age}岁,是一名${work},他喜欢的颜色是${color}!", map);
        System.out.println(mes);
    }
}
