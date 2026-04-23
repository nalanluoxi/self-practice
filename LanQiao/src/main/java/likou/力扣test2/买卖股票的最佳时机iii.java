package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：买卖股票的最佳时机iii
 * @Date：2025/7/1 20:10
 * @Filename：买卖股票的最佳时机iii
 */
public class 买卖股票的最佳时机iii {

    public static void main(String[] args) {
        int[] price1 = {3, 3, 5, 0, 0, 3, 1, 4};
        int[] price2 = {1, 2, 3, 4, 5};
        int[] price3 = {2, 1, 2, 0, 1};
        int[] price = {3, 2, 6, 5, 0, 3};
        System.out.println(maxProfit(price));
        System.out.println(maxProfit2(price));
    }

    public static int maxProfit2(int[] prices) {
        int n = prices.length;
        int[][] dp = new int[n][5];
        dp[0][1] = -prices[0];
        dp[0][3] = -prices[0];
        for (int i = 1; i < n; i++) {
            dp[i][1] = Math.max(dp[i - 1][1], -prices[i]);
            dp[i][2] = Math.max(dp[i - 1][2], dp[i - 1][1] + prices[i]);
            dp[i][3] = Math.max(dp[i - 1][3], dp[i - 1][2] - prices[i]);
            dp[i][4] = Math.max(dp[i - 1][4], dp[i - 1][3] + prices[i]);
        }
        return dp[n - 1][4];
    }

    public static int maxProfit(int[] prices) {
        int[][] dp = new int[prices.length][5];
        dp[0][1] = -prices[0];
        dp[0][3] = -prices[0];
        for (int i = 1; i < prices.length; i++) {
            dp[i][1] = Math.max(dp[i - 1][1], -prices[i]);
            dp[i][2] = Math.max(dp[i - 1][2], dp[i - 1][1] + prices[i]);
            dp[i][3] = Math.max(dp[i - 1][3], dp[i - 1][2] - prices[i]);
            dp[i][4] = Math.max(dp[i - 1][4], dp[i - 1][3] + prices[i]);
        }
        return dp[prices.length - 1][4];
    }
}
