package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：下降路径最小2
 * @Date：2025/6/22 17:14
 * @Filename：下降路径最小2
 */
public class 下降路径最小2 {


    public static int minFallingPathSum(int[][] grid) {
        int n=grid.length;
        int m=grid[0].length;
        int[][]dp=new int[n][m];
        for (int i = 0; i < m; i++) {
            dp[0][i]=grid[0][i];
        }

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < m; j++) {
                dp[i][j]=Integer.MAX_VALUE;
                for (int k = 0; k < m; k++) {
                    if (k!=j){
                        dp[i][j]=Math.min(dp[i][j],dp[i-1][k]+grid[i][j]);
                    }
                }
            }
        }
        int min=Integer.MAX_VALUE;
        for (int i = 0; i < m; i++) {
            min=Math.min(min,dp[n-1][i]);
        }
        return min;
    }
}
