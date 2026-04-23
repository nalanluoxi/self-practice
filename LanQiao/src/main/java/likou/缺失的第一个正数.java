package likou;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：缺失的第一个正数
 * @Date：2025/4/25 17:36
 * @Filename：缺失的第一个正数
 */
public class 缺失的第一个正数 {
    public static void main(String[] args) {
        int[] nums = {1,1};
        //int[] nums = {1,2,0};
        System.out.println(firstMissingPositive(nums));
    }

    public static int firstMissingPositive2(int[] nums) {
        HashSet<Integer> set = new HashSet<>();
        int max =0;
        for (int num : nums) {
            if (num>max){
                max = num;
            }
            set.add(num);
        }
        for (int i = 1; i <= max; i++) {
            if (!set.contains(i)){
                return i;
            }
        }
        return max+1;
    }

    public static int firstMissingPositive1(int[] nums) {
        int min=1;
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            set.add(num);
            while (set.contains(min)){
                min++;
            }
        }
        return min;
    }
    public static int firstMissingPositive(int[] nums) {
        Arrays.sort(nums);
        int ans=1;
        int i=0;
        while (i<nums.length){

            if (nums[i]>ans){
                return ans;
            }
            if (nums[i]==ans){
                ans++;
                i++;
            } else if (nums[i]<=0) {
                i++;
            }else if (nums[i]==nums[i-1]){
                i++;
            }
        }
        return ans;
    }


    }
