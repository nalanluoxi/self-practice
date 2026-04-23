package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：最大正方形
 * @Date：2025/5/9 12:10
 * @Filename：最大正方形
 */
public class 最大正方形 {
    public static void main(String[] args) {
       /* char[][]nums={
                {'1','0','1','0','0'},
                {'1','0','1','1','1'},
                {'1','1','1','1','1'},
                {'1','0','0','1','0'}
        };*/
      /*  char[][]nums={
                {'0','1'},
                {'1','0'}
        };*/
/*        char[][]nums={
                {'0','1'}
        };*/
        char[][] nums = {
                {'1', '1', '1', '1', '0'},
                {'1', '1', '1', '1', '0'},
                {'1', '1', '1', '1', '1'},
                {'1', '1', '1', '1', '1'},
                {'0', '0', '1', '1', '1'}
        };
        System.out.println(maximalSquare(nums));
    }

    /*static int ans;

    public static int maximalSquare(char[][] matrix) {
        ans = 0;
        int m = matrix.length;
        int n = matrix[0].length;
        int[] dp = new int[n];
        for (int i = 0; i < n; i++) {
            dp[i] = matrix[0][i] - '0';
            ans = Math.max(ans, dp[i]);
        }
        for (int i = 1; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int temp = matrix[i][j] - '0';
                dp[j] = dp[j] == 0 ? 0 : dp[j] + temp;
            }
            for (int j = 0; j < m; j++) {
                if (isTrue(dp, j)) {
                    ans = Math.max(ans, dp[j]*dp[j]);
                }
            }
        }
        return ans;
    }
    public static boolean isTrue(int[] nums, int index) {
        int l = nums[index];
        if (nums.length - index <= l) {
            return false;
        }
        for (int i = index+1; i < index+l; i++) {
            if (nums[i]!=l){
                return false;
            }
        }
        return true;
    }*/

    static int ans;
    static int[][]dp;

    public static int maximalSquare(char[][] matrix) {
        ans=0;
        init(matrix);
        help(matrix);
        return ans*ans;
    }

    public static void  init(char[][]nums){
        dp=new int[nums.length][nums[0].length];
        for (int i = 0; i < nums.length; i++) {
            if (nums[i][0]=='1'){
                dp[i][0]=1;
                ans=1;
            }
        }
        for (int i = 0; i < nums[0].length; i++) {
            if (nums[0][i]=='1'){
                dp[0][i]=1;
                ans=1;
            }
        }
    }

    public static void help(char[][]nums){
        for (int i = 1; i < nums.length; i++) {
            for (int j = 1; j < nums[0].length; j++) {
                if (nums[i][j]=='0'){
                    continue;
                } else if (nums[i][j]=='1') {
                    dp[i][j]=Math.min(dp[i-1][j],Math.min(dp[i-1][j-1],dp[i][j-1]))+1;
                    ans=Math.max(ans,dp[i][j]);
                }
            }
        }
    }
}
