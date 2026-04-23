package likou.动态规划;

import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：最长递增子序列
 * @Date：2025/6/6 16:14
 * @Filename：最长递增子序列
 */
public class 最长递增子序列 {

    public static void main(String[] args) {
        System.out.println(lengthOfLIS(new int[]{10,9,2,5,3,7,101,18}));
    }

    static int[] dp;
    static int[] nums;
    public static int lengthOfLIS(int[] num) {
        if (num==null||num.length==0){
            return 0;
        }
        if (num.length==1){
            return 1;
        }
        int ans=0;
        nums=num;
        dp=new int[nums.length];
        dp[0]=1;
        for (int i = 0; i < num.length; i++) {
            ans=Math.max(ans,dfs(i));
        }
        return ans;
    }
    public static int dfs(int index){
        if (dp[index]!=0){
            return dp[index];
        }
        dp[index]=1;
        for (int i = 0; i <index; i++) {
            if (nums[index]>nums[i]){
                dp[index]=Math.max(dp[index],dfs(i)+1);
            }
        }
        return dp[index];
    }
   /* static int[]dp;
    public static int lengthOfLIS(int[] nums) {
        dp=new int[nums.length];
        dp[0]=1;
        int ans=0;
        for (int i = 0; i < nums.length; i++) {
            ans= Math.max(ans,dfs(i,nums));
        }
        return ans;
    }
    public static int dfs(int index,int[] nums){
        if (dp[index]!=0){
            return dp[0];
        }
        dp[index]=1;
        for (int i=0;i<index;i++){
            if (nums[index]>nums[i]){
                dp[index]=Math.max(dp[index],dfs(i,nums)+1);
            }
        }
        return dp[index];
    }*/
}
