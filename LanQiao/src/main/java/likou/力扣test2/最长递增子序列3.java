package likou.力扣test2;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：最长递增子序列3
 * @Date：2025/6/27 17:37
 * @Filename：最长递增子序列3
 */
public class 最长递增子序列3 {

    public static void main(String[] args) {
        int[]num={10,9,2,5,3,7,101,18};
        int[]num1={3,2,1};
        int[]num3={0,1,0,3,2,3};
        System.out.println(lengthOfLIS(num));
    }
    public static int lengthOfLIS(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        int ans=1;
        dp[0]=1;
        for (int i = 1; i < n; i++) {
            dp[i]=1;
            for (int j = 0; j < i; j++) {
                if(nums[i]>nums[j]){
                    dp[i]=Math.max(dp[i],dp[j]+1);
                }
            }
            ans=Math.max(ans,dp[i]);
        }
        return ans;
    }
}
