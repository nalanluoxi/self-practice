package likou.力扣test2;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：两数之和
 * @Date：2025/6/4 17:48
 * @Filename：两数之和
 */
public class 两数之和 {

    public static void main(String[] args) {
       // int[]nums={3,2,4};
        int[]nums={3,1,5,3};
        int[] ints = twoSum(nums, 6);
        for (int i = 0; i < ints.length; i++) {
            System.out.println(ints[i]);
        }
    }
    public static int[] twoSum(int[] nums, int target) {
        int[] ans=new int[2];
        Map<Integer,Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i],i);
        }
        for (int i = 0; i < nums.length; i++) {
            int t = target - nums[i];
            if (map.containsKey(t) && map.get(t)!=i){
                ans[0]=i;
                ans[1]=map.get(t);
                return ans;
            }
        }
        return ans;
    }


}
