package com.leetcode.editor.cn;
//给你两个二进制字符串，返回它们的和（用二进制表示）。 
//
// 输入为 非空 字符串且只包含数字 1 和 0。 
//
// 
//
// 示例 1: 
//
// 输入: a = "11", b = "1"
//输出: "100" 
//
// 示例 2: 
//
// 输入: a = "1010", b = "1011"
//输出: "10101" 
//
// 
//
// 提示： 
//
// 
// 每个字符串仅由字符 '0' 或 '1' 组成。 
// 1 <= a.length, b.length <= 10^4 
// 字符串如果不是 "0" ，就都不含前导零。 
// 
// Related Topics 数学 字符串 
// 👍 496 👎 0

import java.util.Stack;

public class P67AddBinary{
    //leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public String addBinary(String a, String b) {
        String max = "";
        String min = "";
        if (a.length()>=b.length()){
            max = a;
            min = b;
        }else {
            max = b;
            min = a;
        }
        int lengthMax = max.length() - 1;
        int lengthMin = min.length() - 1;
        int jin = 0;
        StringBuffer result = new StringBuffer();
        while (lengthMax>=0){
            int ca= (char) max.charAt(lengthMax)%48;
            int cb = 0;
            if (lengthMin>=0){
                cb= (char) min.charAt(lengthMin)%48;
            }
            int tmp = jin + ca + cb;
            if ( tmp == 2){
                result.insert(0, "0");
                jin = 1;
            }else if (tmp == 3){
                result.insert(0, "1");
                jin = 1;
            } else {
                result.insert(0, tmp);
                jin = 0;
            }
            lengthMax --;
            lengthMin --;
        }
        if (jin == 1){
            result.insert(0, "1");
        }
        return result.toString();
    }
}
//leetcode submit region end(Prohibit modification and deletion)

}