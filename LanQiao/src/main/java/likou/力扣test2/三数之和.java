package likou.力扣test2;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：三数之和
 * @Date：2025/6/6 9:38
 * @Filename：三数之和
 */
public class 三数之和 {
    public static void main(String[] args) {
        int[] nums={-1,0,1,2,-1,-4};
        System.out.println(threeSum(nums));
    }

    static List<List<Integer>> ans;

    public static List<List<Integer>> threeSum(int[] nums) {
        if (nums.length<3){
            return null;
        }
        ans=new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            int a = nums[i];
            if (a>0){
                break;
            }
            if (i>0&&nums[i]==nums[i-1]){
                continue;
            }
            int l=i+1;
            int r=nums.length-1;
            while (l<r){
                int sum = nums[i] + nums[l] + nums[r];
                if (sum==0){
                    ans.add(Arrays.asList(nums[i],nums[l],nums[r]));
                    while (l<r && nums[l]==nums[l+1]){
                        l++;
                    }
                    while (l<r && nums[r]==nums[r-1]){
                        r--;
                    }
                    l++;
                    r--;
                } else if (sum>0) {
                    r--;
                } else if (sum < 0) {
                    l++;
                }
            }
        }
        return ans;
    }



}
