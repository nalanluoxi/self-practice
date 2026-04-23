package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：整数拆分
 * @Date：2025/3/11 19:15
 * @Filename：整数拆分
 */
public class 整数拆分 {
    public static void main(String[] args) {
        System.out.println(integerBreak(10));
    }

    static int[] dp;

    public static int integerBreak(int n) {
        dp = new int[n + 1];
        dp[2] = 1;
        for (int i = 3; i <= n; i++) {
            for (int j = 1; j <= i - j; j++) {
                dp[i] = Math.max(dp[i], Math.max(j * (i - j), j * dp[i - j]));
            }
        }
        return dp[n];
    }
}
