package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：买卖股票的最佳时机
 * @Date：2025/6/4 19:39
 * @Filename：买卖股票的最佳时机
 */
public class 买卖股票的最佳时机 {


    public static void main(String[] args) {
        System.out.println(maxProfit(new int[]{7, 1, 5, 3, 6, 4}));
    }
    public static int maxProfit(int[] prices) {
        int[]dp=new int[prices.length+1];
        int ans=0;
        for (int i = prices.length-1; i >= 0; i--) {
            dp[i]=Math.max(prices[i],dp[i+1]);
            ans=Math.max(ans,dp[i]-prices[i]);
        }
        return ans;
    }
}
