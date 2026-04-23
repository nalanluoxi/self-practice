package likou.力扣test2;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：零钱兑换
 * @Date：2025/6/8 17:13
 * @Filename：零钱兑换
 */
public class 零钱兑换 {


    public static void main(String[] args) {
        int[] coins = {1, 2, 5};
        int amount = 11;
        System.out.println(coinChange(coins, amount));//3
    }

    static int[] dp;
    static int[] arrs;

    public static int coinChange(int[] coins, int amount) {
        Arrays.sort(coins);
        int len = coins.length;
        arrs = new int[len];
        for (int i = 0; i < len; i++) {
            arrs[i] = coins[i];
        }
        dp = new int[amount + 1];
        return dfs(amount);

    }

    public static int dfs(int target) {
        if (target<0){
            return -1;
        }
        if (target == 0) {
            return 0;
        }
        if (dp[target] != 0) {
            return dp[target];
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < arrs.length; i++) {
            if (arrs[i]>target){
                continue;
            }else {
                int temp = dfs(target - arrs[i]);
                if (temp != -1&&temp<min) {
                    min=temp+1;
                }
            }
        }
        dp[target]=min==Integer.MAX_VALUE?-1:min;
        return dp[target];
    }
}

