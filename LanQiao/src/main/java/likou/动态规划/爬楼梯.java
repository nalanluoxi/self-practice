package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：爬楼梯
 * @Date：2025/3/10 21:31
 * @Filename：爬楼梯
 */
public class 爬楼梯 {
    public static void main(String[] args) {
        System.out.println(climbStairs(4));
    }

    static int[] dp;
    public static int climbStairs(int n) {
        if (n<=2){
            return n;
        }
        dp=new int[n+1];
        dp[1]=1;
        dp[2]=2;
        for (int i = 3; i <= n; i++) {
            dp[i]=dp[i-1]+dp[i-2];
        }
        return dp[n];
    }
}
