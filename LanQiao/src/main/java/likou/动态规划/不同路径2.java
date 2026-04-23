package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：不同路径2
 * @Date：2025/3/11 11:54
 * @Filename：不同路径2
 */
public class 不同路径2 {
    public static void main(String[] args) {
        int[][] nums = {{0,0,0},{0,1,0},{0,0,0}};
        System.out.println(uniquePathsWithObstacles(nums));
    }

    static int[][] dp;
    public static int uniquePathsWithObstacles(int[][] nums) {
        int m = nums.length;
        int n = nums[0].length;
        dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            if (nums[i][0] == 0) {
                dp[i][0] = 1;
            }else if (nums[i][0] == 1){
                break;
            }
        }
        for (int i = 0; i < n; i++) {
            if (nums[0][i] == 0) {
                dp[0][i] = 1;
            }else if (nums[0][i] == 1){
                break;
            }
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (nums[i][j] == 0) {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                }
            }
        }
        return dp[m - 1][n - 1];
    }
}
