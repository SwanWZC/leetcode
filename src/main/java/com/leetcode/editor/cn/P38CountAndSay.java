package com.leetcode.editor.cn;
//给定一个正整数 n（1 ≤ n ≤ 30），输出外观数列的第 n 项。 
//
// 注意：整数序列中的每一项将表示为一个字符串。 
//
// 「外观数列」是一个整数序列，从数字 1 开始，序列中的每一项都是对前一项的描述。前五项如下： 
//
//1.     1
//2.     11
//3.     21
//4.     1211
//5.     111221
//6.     312211
//7.     13112221
//8.     1113213211
// 
//
// 第一项是数字 1 
//
// 描述前一项，这个数是 1 即 “一个 1 ”，记作 11 
//
// 描述前一项，这个数是 11 即 “两个 1 ” ，记作 21 
//
// 描述前一项，这个数是 21 即 “一个 2 一个 1 ” ，记作 1211 
//
// 描述前一项，这个数是 1211 即 “一个 1 一个 2 两个 1 ” ，记作 111221 
//
// 
//
// 示例 1: 
//
// 输入: 1
//输出: "1"
//解释：这是一个基本样例。 
//
// 示例 2: 
//
// 输入: 4
//输出: "1211"
//解释：当 n = 3 时，序列是 "21"，其中我们有 "2" 和 "1" 两组，"2" 可以读作 "12"，也就是出现频次 = 1 而 值 = 2；类似 
//"1" 可以读作 "11"。所以答案是 "12" 和 "11" 组合在一起，也就是 "1211"。 
// Related Topics 字符串 
// 👍 561 👎 0

public class P38CountAndSay{
    //leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public String countAndSay(int n) {
        if (n == 1) return "1";
        if (n == 2) return "11";
        StringBuffer stringBuffer = new StringBuffer("11");
        stringBuffer.append("A");
        return countAndSay(stringBuffer,2,n).toString();
    }

    private StringBuffer countAndSay(StringBuffer str,int mark,int n){
        mark ++;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str.length()-1; i++) {
            int count = 1;
            for (int j = i+1; j < str.length(); j++) {
                if (str.charAt(i) == str.charAt(j)){
                    count++;
                }else {
                    stringBuffer.append(count);
                    stringBuffer.append(str.charAt(i));
                    break;
                }
            }
            i = i+count-1;
        }
        if (mark != n){
            stringBuffer.append("A");
            stringBuffer = countAndSay(stringBuffer,mark,n);
        }
        return stringBuffer;
    }
}
//leetcode submit region end(Prohibit modification and deletion)
//public static void main(String[] args) {
//    P38CountAndSay p38CountAndSay = new P38CountAndSay();
//    System.out.println(p38CountAndSay.countAndSay(7));
//}
}