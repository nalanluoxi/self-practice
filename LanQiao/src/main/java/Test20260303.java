import java.util.Arrays;

public class Test20260303 {
    public static void main(String[] args) {

/// 给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
/// 计算并返回可以凑成总金额所需的 最少的硬币个数 。如果没有任何一种硬币组合能组成总金额，返回 -1 。
/// 你可以认为每种硬币的数量是无限的。
/// 示例 1：
/// 输入：coins =
        int []coins ={1,2,3};
        int target =5;
        System.out.println(way(coins,target));

    }

    public static int way(int []nums,int target){
        Arrays.sort(nums);
        int[]dp=new int[target+1];
        Arrays.fill(dp,Integer.MAX_VALUE-1);
        dp[0]=0;
        for (int i = 1; i <= target; i++) {
            for (int num : nums) {
                if (i>=num && dp[i-num]!=Integer.MAX_VALUE-1){
                    dp[i]=Math.min(dp[i],dp[i-num]+1);
                }
            }
        }

        return dp[target]==Integer.MAX_VALUE-1?-1:dp[target];
    }


}
