package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：买卖股票的最佳时机2
 * @Date：2025/6/21 20:42
 * @Filename：买卖股票的最佳时机2
 */
public class 买卖股票的最佳时机2 {
    public static void main(String[] args) {
        int [] prices=new int[]{3,3,5,0,0,3,1,4};
        System.out.println(maxProfit(prices));
    }
    public static int maxProfit(int[] prices) {
        int n=prices.length;
        int[][]dp=new int[n+1][5];
        dp[0][1]=-prices[0];
        dp[0][3]=-prices[0];
        for(int i=1;i<n;i++){
            dp[i][1]=Math.max(dp[i-1][1],-prices[i]);
            dp[i][2]=Math.max(dp[i-1][2],dp[i-1][1]+prices[i]);
            dp[i][3]=Math.max(dp[i-1][2]-prices[i],dp[i-1][3]);
            dp[i][4]=Math.max(dp[i-1][3],dp[i-1][3]+prices[i]);
        }
        return dp[n-1][4];
    }
}
