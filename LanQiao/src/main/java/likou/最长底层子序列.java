package likou;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：最长底层子序列
 * @Date：2025/2/11 17:57
 * @Filename：最长底层子序列
 */
public class 最长底层子序列 {
    public static void main(String[] args) {
        System.out.println(lengthOfLIS(new int[]{10,9,2,5,3,7,101,18}));
    }


    static int []dp;
    public static int lengthOfLIS(int[] nums) {
        dp=new int[nums.length];
        Arrays.fill(dp,-1);
        for (int i = nums.length-1; i>=0; i--) {
            dps(nums,i);
            //System.out.println("dp["+i+"] = " + dp[i]);
        }
        int max=-1;
        for (int i = 0; i < dp.length; i++) {
            max=Math.max(max,dp[i]);
        }
        return max;
    }

    public static int dps(int [] nums,int index){
        if (index== nums.length){
            return 0;
        }
        if (dp[index]!=-1){
            return dp[index];
        }
        int tempmax=-1;
        for (int i = index+1; i < nums.length; i++) {
            if (nums[i]>nums[index]){
                tempmax=Math.max(tempmax, dps(nums,i)+1);
            }
        }
        dp[index]=tempmax==-1?1:tempmax;
        return dp[index];
    }
}
