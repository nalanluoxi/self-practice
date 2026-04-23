package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：不同路径
 * @Date：2025/3/10 22:38
 * @Filename：不同路径
 */
public class 不同路径 {
    public static void main(String[] args) {
        System.out.println(uniquePaths(3, 7));
    }

    static int[][] dp;
    public static int uniquePaths(int m, int n) {
        dp=new int[m][n];
        dp[0][0]=1;
        for (int i = 0; i < m; i++) {
            dp[i][0]=1;
        }
        for (int i = 0; i < n; i++) {
            dp[0][i]=1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j]=dp[i-1][j]+dp[i][j-1];
            }
        }
        return dp[m-1][n-1];
    }
}
