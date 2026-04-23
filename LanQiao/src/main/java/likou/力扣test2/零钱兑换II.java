package likou.力扣test2;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：零钱兑换II
 * @Date：2025/7/1 20:38
 * @Filename：零钱兑换II
 */
public class 零钱兑换II {

    public static void main(String[] args) {
        int[]coins={1,2,5};
        System.out.println(change(5,coins));
    }
    public static int change(int amount, int[] coins) {
        if (amount==0){
            return 1;
        }
        int[]dp=new int[amount+1];
        dp[0]=1;
        Arrays.sort(coins);
        for (int coin : coins) {
            for (int i = 1; i <= amount; i++) {
                if (coin>i){
                    continue;
                }
                dp[i]+=dp[i-coin];
            }
        }
        return dp[amount];
    }
}
