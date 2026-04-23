package likou.动态规划;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：零钱兑换2
 * @Date：2025/6/21 21:00
 * @Filename：零钱兑换2
 */
public class 零钱兑换2 {
    public static void main(String[] args) {
        int amount = 5;
        int[] coins = new int[]{1, 2, 5};

        /*int amount=500;
        int [] coins=new int[]{3,5,7,8,9,10,11};*/
        /**
         * 5
         * 221
         * 2111
         * 11111
         */

        System.out.println(change(amount, coins));
    }

    public static int change(int amount, int[] coins) {
        int[] dp = new int[amount + 1];
        Arrays.sort(coins);
        dp[0]=1;
        for (int coin : coins) {
            for (int i = 1; i <= amount; i++) {
                if (i - coin >= 0) {
                    dp[i] += dp[i - coin];
                }
            }
        }
        return dp[amount];
    }

   /* static int[]coin;
    static  int target;
    static int ans;
    public static int change(int amount, int[] coins) {
        coin=coins;
        target=amount;
        ans=0;
        dfs(coin.length-1, 0);
        return ans;
    }

    public static void dfs(int index,int sum){
        if (sum==target){
            ans++;
        }
        if (index<0){
            return ;
        }
        for(int i=index;i>=0;i--){
            if (coin[i]<=target-sum){
                dfs(i,sum+coin[i]);
            }
        }
    }
*/


}
