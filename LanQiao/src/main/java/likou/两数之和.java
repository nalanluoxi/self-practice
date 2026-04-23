package likou;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：两数之和
 * @Date：2025/3/24 10:38
 * @Filename：两数之和
 */
public class 两数之和 {
    public static void main(String[] args) {
        int[] ints = twoSum(new int[]{2, 7, 11, 15}, 9);
        for (int i = 0; i < ints.length; i++) {
            System.out.println(ints[i]);
        }
    }

    public static int[] twoSum(int[] nums, int target) {
        int []ans=new int[2];
        Map<Integer,Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i],i);
        }
        for (int i = 0; i < nums.length; i++) {
            int temp = target - nums[i];
            if (map.containsKey(temp) && !map.get(temp).equals(i)){
                return new int[]{i,map.get(temp)};
            }
        }
        return nums;
    }
}
