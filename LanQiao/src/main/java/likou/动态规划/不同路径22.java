package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：不同路径22
 * @Date：2025/6/21 21:48
 * @Filename：不同路径22
 */
public class 不同路径22 {


    public static int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m=obstacleGrid.length;
        int n=obstacleGrid[0].length;
        int [][]dp=new int[m][n];
        for (int i = 0; i < n; i++) {
            if(obstacleGrid[0][i]==1){
                break;
            }
            dp[0][i]=1;
        }
        for (int i = 0; i < m; i++) {
            if(obstacleGrid[i][0]==1){
                break;
            }
            dp[i][0]=1;
        }

        for(int i=1;i<m;i++){
            for (int j = 1; j < n; j++) {
                if (obstacleGrid[i][j]==1){
                    continue;
                }
                dp[i][j]=dp[i-1][j]+dp[i][j-1];
            }
        }
        return dp[m-1][n-1];
    }
}
