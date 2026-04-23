package likou.力扣test2;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：零钱兑换2
 * @Date：2025/7/1 19:41
 * @Filename：零钱兑换2
 */
public class 零钱兑换2 {
    public static void main(String[] args) {
        System.out.println(coinChange(new int[]{1,2,5}, 11));
        System.out.println(coinChange(new int[]{1}, 0));
        System.out.println(coinChange(new int[]{2}, 3));
    }

    static int[]dp;
    static int[]coin;
    public static int coinChange(int[] coins, int amount) {
        coin=coins;
        dp=new int[amount+1];
        Arrays.sort(coin);
        //Arrays.fill(dp,-1);
        return dfs(amount);
    }

    public static int dfs(int target){
        if (target<0){
            return -1;
        }
        if (target==0){
            return 0;
        }
        if (dp[target]!=0){
            return dp[target];
        }
        int t=Integer.MAX_VALUE;
        for (int co : coin) {
            if (co>target){
                break;
            }
            int dfs = dfs(target - co);
            if (dfs!=-1 && dfs<t){
                t=dfs+1;
            }
        }
        dp[target]= t==Integer.MAX_VALUE?-1:t;
        return dp[target];
    }
}
