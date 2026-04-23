package likou.力扣test2;

import java.sql.SQLOutput;
import java.util.Arrays;

public class Test0316 {
    public static void main(String[] args) {
        //System.out.println(longestValidParentheses("())"));

        /*int[]nums={1,2,5};
        System.out.println(coinChange(nums,11));*/

       /* char[][]nums={
                {'1','0','1','0','0'},
                {'1','0','1','1','1'},
                {'1','1','0','1','1'},
                {'1','1','1','1','1'}

        };*/
        char[][]nums={
                {'1','0'},
                {'0','1'}

        };
        System.out.println(maximalSquare(nums));
    }

    public static int maximalSquare(char[][] nums) {
        int n = nums.length;
        int m = nums[0].length;
        int[][]dp=new int[n][m];
        int ans=0;
        for (int i = 0; i < n; i++) {
            if (nums[i][0]=='1'){
                dp[i][0]=1;
            }
            ans=Math.max(ans,dp[i][0]);
        }

        for (int i = 0; i < m; i++) {
            if (nums[0][i]=='1'){
                dp[0][i]=1;
            }
            ans=Math.max(ans,dp[i][0]);
        }




        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                if (nums[i][j]=='0'){
                    continue;
                }else {
                    dp[i][j]=Math.min(dp[i-1][j-1],Math.min(dp[i-1][j],dp[i][j-1]))+1;
                }
                ans=Math.max(ans,dp[i][j]);
            }
        }

        return ans*ans;
    }


    public static int minPathSum(int[][] nums) {
        int m = nums.length;
        int n = nums[0].length;
        int[][]dp=new int[m][n];

        dp[0][0]=nums[0][0];
        for (int i = 1; i < m; i++) {
            dp[i][0]=nums[i][0]+dp[i-1][0];
        }

        for (int i = 1; i < n; i++) {
            dp[0][i]=nums[0][i]+dp[0][i-1];
        }

        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                dp[i][j]=Math.min(dp[i-1][j],dp[i][j-1])+nums[i][j];
            }
        }

        return dp[m-1][n-1];
    }


    public static int coinChange(int[] coins, int amount) {
        int[]dp=new int[amount+1];
       // dp[0]=1;
        Arrays.sort(coins);
        for (int i = 1; i <= amount; i++) {
            dp[i]=Integer.MAX_VALUE;
            for (int c : coins) {
                if (i-c>=0 && dp[i-c]!=Integer.MAX_VALUE ){
                    dp[i]=Math.min(dp[i-c]+1,dp[i]);
                }
            }
        }
        return dp[amount]==Integer.MAX_VALUE?-1:dp[amount];
    }


    public static int longestValidParentheses(String s) {
        int len = s.length();
        if ( len<=1){
            return 0;
        }
        int[]dp=new int[len];

        int ans=0;
        for (int i = 0; i < len; i++) {
            if (i==0 || s.charAt(i)=='('){
                continue;
            }else {
                int befor = dp[i - 1];
                if (i-befor-1>=0 && s.charAt(i-befor-1)=='('){
                    dp[i]=2+befor;

                    if (i-befor-2>=0){
                        dp[i]+=dp[i-befor-2];
                    }
                }
            }

            ans=Math.max(ans,dp[i]);
        }

        return ans;
    }
}
