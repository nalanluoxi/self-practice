package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：最小花费爬楼梯
 * @Date：2025/3/10 21:59
 * @Filename：最小花费爬楼梯
 */
public class 最小花费爬楼梯 {
    public static void main(String[] args) {
        int[] cost = {1, 100, 1, 1, 1, 100, 1, 1, 100, 1};
        System.out.println(minCostClimbingStairs(cost));
    }

    static int[] dp;
    public static int minCostClimbingStairs(int[] cost) {
        if (cost.length<=2){
            return Math.min(cost[0],cost[1]);
        }
        dp=new int[cost.length+1];
        dp[1]=cost[0];
        dp[2]=cost[1];
        for (int i = 3; i <= cost.length; i++) {
            dp[i]=cost[i-1]+Math.min(dp[i-1],dp[i-2]);
        }
        return Math.min(dp[cost.length],dp[cost.length-1]);
    }
}
