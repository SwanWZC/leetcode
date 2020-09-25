package com.leetcode.editor.cn;
//给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。 
//
// 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。 
//
// 
//
// 示例: 
//
// 给定 nums = [2, 7, 11, 15], target = 9
//
//因为 nums[0] + nums[1] = 2 + 7 = 9
//所以返回 [0, 1]
// 
// Related Topics 数组 哈希表 
// 👍 9202 👎 0

import java.util.HashMap;
import java.util.Map;

public class P1TwoSum{
    //leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer,Integer> map = new HashMap<Integer, Integer>();
        int[] result = new int[2];
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i])){
                if (nums[i]*2 == target){
                    result[0] = map.get(nums[i]);
                    result[1] = i;
                    return result;
                }
            }else {
                map.put(nums[i],i);

            }
        }
        for (int key :map.keySet()) {
            if (map.containsKey(target-key)){
                result[0] = map.get(target-key);
                result[1] = map.get(key);
            }
        }
        return result;
    }
}
//leetcode submit region end(Prohibit modification and deletion)

}