public class CoinChange2 {

    /**
     * 计算可以凑成总金额的硬币组合数。
     * 这是一个典型的动态规划问题（完全背包）。
     *
     * @param amount 总金额
     * @param coins  硬币面额数组
     * @return 组合数
     */
    public int change(int amount, int[] coins) {
        // dp[i] 表示金额之和为 i 的硬币组合数
        int[] dp = new int[amount + 1];

        // base case: 凑成金额0的组合数为1（即不选用任何硬币）
        dp[0] = 1;

        // 正确的循环顺序：先遍历物品（硬币），再遍历背包（金额）
        // 这样可以确保组合的唯一性，避免重复计算
        for (int coin : coins) {
            // 从 coin 开始遍历金额，因为小于 coin 的金额无法由当前的 coin 凑成
            for (int j = coin; j <= amount; j++) {
                // 正确的状态转移方程
                // dp[j] 的值由两部分组成：
                // 1. 不使用当前 coin 凑成 j 的组合数（继承自上一个硬币循环的 dp[j]）
                // 2. 使用当前 coin 凑成 j 的组合数（其值为 dp[j - coin]）
                dp[j] += dp[j - coin];
            }
        }

        return dp[amount];
    }

    public static void main(String[] args) {
        CoinChange2 solution = new CoinChange2();

        // 示例 1
        int amount1 = 5;
        int[] coins1 = {1, 2, 5};
        System.out.println("输入: amount = " + amount1 + ", coins = " + java.util.Arrays.toString(coins1));
        System.out.println("输出: " + solution.change(amount1, coins1)); // 预期: 4
        System.out.println();

        // 示例 2
        int amount2 = 3;
        int[] coins2 = {2};
        System.out.println("输入: amount = " + amount2 + ", coins = " + java.util.Arrays.toString(coins2));
        System.out.println("输出: " + solution.change(amount2, coins2)); // 预期: 0
        System.out.println();

        // 示例 3
        int amount3 = 10;
        int[] coins3 = {10};
        System.out.println("输入: amount = " + amount3 + ", coins = " + java.util.Arrays.toString(coins3));
        System.out.println("输出: " + solution.change(amount3, coins3)); // 预期: 1
        System.out.println();

        // 边界情况：amount = 0
        int amount4 = 0;
        int[] coins4 = {1, 2, 5};
        System.out.println("输入: amount = " + amount4 + ", coins = " + java.util.Arrays.toString(coins4));
        System.out.println("输出: " + solution.change(amount4, coins4)); // 预期: 1
    }
} 