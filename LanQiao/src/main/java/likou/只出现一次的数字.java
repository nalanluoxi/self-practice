package likou;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：只出现一次的数字
 * @Date：2025/5/13 21:03
 * @Filename：只出现一次的数字
 */
public class 只出现一次的数字 {
    public static void main(String[] args) {
        int[] nums={4,1,2,1,2};
        System.out.println(singleNumber(nums));
    }
    /*public static int singleNumber(int[] nums) {
        Map<Integer,Integer> map=new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i],map.getOrDefault(nums[i],0)+1);
        }
        for (Integer key : map.keySet()) {
            if (map.get(key)==1){
                return key;
            }
        }
        return -1;
    }*/
    public static int singleNumber(int[] nums) {
       int ans=0;
        for (int i = 0; i < nums.length; i++) {
            ans^=nums[i];
            System.out.println("i="+i+"时ans："+ans);
        }
        return ans;
    }
}
