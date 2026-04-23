package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：下降路径最小和
 * @Date：2025/6/21 22:26
 * @Filename：下降路径最小和
 */
public class 下降路径最小和 {
    public static void main(String[] args) {
        int [][]matrix=new int[][]{{2,1,3},{6,5,4},{7,8,9}};
        System.out.println(minFallingPathSum(matrix));
    }
    public static int minFallingPathSum(int[][] matrix) {
        int n=matrix.length;
        int []dp=new int[n];
        for (int i = 0; i < n; i++) {
            dp[i]=matrix[0][i];
        }
        for (int i = 1; i < n; i++) {
            int []newdp=new int[n];
            for (int j = 0; j < matrix[0].length; j++) {
                if (j==0){
                    newdp[j]=Math.min(dp[j],dp[j+1])+matrix[i][j];
                } else if (j==matrix[0].length-1) {
                    newdp[j]=Math.min(dp[j],dp[j-1])+matrix[i][j];
                }else {
                    newdp[j]=Math.min(dp[j],Math.min(dp[j-1],dp[j+1]))+matrix[i][j];
                }
            }
            dp=newdp;
        }
        int min=Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            min=Math.min(min,dp[i]);
        }
        return min;
    }
}
