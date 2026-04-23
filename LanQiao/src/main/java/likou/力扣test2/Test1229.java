package likou.力扣test2;

import javax.print.DocFlavor;
import java.util.Arrays;

public class Test1229 {


    public static void main(String[] args) {


        System.out.println(longestPalindrome("cbbd"));

    }


    public static int mySqrt(int x) {

        return 0;
    }

    public static int uniquePathsWithObstacles(int[][] nums) {
        int n = nums.length;
        int m = nums[0].length;
        int[][] dp = new int[n][m];
        if (nums[0][0]==0){
            dp[0][0] = 1;
        }
        for (int i = 1; i < n; i++) {
            if (nums[i][0]==0){
                dp[i][0]=1*dp[i-1][0];
            }
        }
        for (int i = 1; i < m; i++) {
            if (nums[0][i]==0){
                dp[0][i]=1*dp[0][i-1];
            }
        }
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                if (nums[i][j]==0){
                    dp[i][j]=dp[i-1][j]+dp[i][j-1];
                }
            }
        }
        return dp[n-1][m-1];
    }

    public static int minPathSum(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        int[][]dp=new int[n][m];
        for (int i = 0; i < n; i++) {
            if (i==0){
                dp[i][0]=grid[0][0];
            }else {
                dp[i][0]=dp[i-1][0]+grid[i][0];
            }
        }
        for (int i = 0; i < m; i++) {
            if (i==0){
                dp[0][i]=grid[0][0];
            }else {
                dp[0][i]=dp[0][i-1]+grid[0][i];
            }
        }
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                dp[i][j]=Math.min(dp[i-1][j],dp[i][j-1])+grid[i][j];
            }
        }
        return dp[n-1][m-1];
    }

    public static boolean searchMatrix(int[][] matrix, int target) {
        int n = matrix.length;
        int m = matrix[0].length;
        int x=n-1,y=0;
        while (x>=0 && y<m){
            if (matrix[x][y]==target){
                return true;
            }else if (matrix[x][y]<target){
                y++;
            }else {
                x--;
            }
        }
        return false;
    }


    public static int findKthLargest(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length - k];
    }


    public static String longestPalindrome(String s) {
        int len = s.length();
        int start =0;
        int maxlen=1;
        boolean[][]dp=new boolean[ len][len] ;
        for (int i = 0; i < len; i++) {
            dp[i][i] = true;
        }
        for (int l = 1; l <= len; l++) {
            for (int i = 0; i <= len; i++) {
                int end = i + l;
                if (end >= len){
                    continue;
                }
                if (s.charAt(i)==s.charAt(end)){
                    if (l<=2){
                        dp[i][end] = true;
                    }else {
                        dp[i][end]=dp[i+1][end-1];
                    }

                }else {
                    dp[i][end]=false;
                }
                if (dp[i][end] && l>=maxlen){
                        maxlen=l+1;
                        start=i;
                }
            }
        }
        return s.substring(start,start+maxlen);
    }

    public static int minDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();
        if (len1 * len2==0){
            return len2+len1;
        }
        int[][]dp=new int[len1+1][len2+1];
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (word1.charAt(i-1)==word2.charAt(j-1)){
                    dp[i][j]=dp[i-1][j-1];
                }else {
                    dp[i][j]=Math.min(dp[i-1][j-1],Math.min(dp[i-1][j],dp[i][j-1]))+1;
                }
            }
        }
        return dp[len1][len2];
    }


    public static int maximalSquare(char[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;
        int[][] dp = new int[n][m];
        int ans=0;
        for (int i = 0; i < n; i++) {
            if (matrix[i][0] == '1') {
                dp[i][0] = 1;
            }
            ans = Math.max(ans, dp[i][0]);
        }
        for (int i = 1; i < m; i++) {
            if (matrix[0][i] == '1') {
                dp[0][i] = 1;
            }
            ans = Math.max(ans, dp[0][i]);
        }
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                if (matrix[i][j]=='0'){
                    dp[i][j]=0;
                }else {
                    dp[i][j]=Math.min(dp[i-1][j-1],Math.min(dp[i-1][j],dp[i][j-1]))+1;
                }
                ans = Math.max(ans,dp[i][j]);
            }
        }
        return ans*ans;
    }


}
