package likou.力扣test2;

import ch.qos.logback.core.read.ListAppender;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：Test0925
 * @Date：2025/9/25 22:49
 * @Filename：Test0925
 */
public class Test0925 {

    public static void main(String[] args) {
        /*System.out.println(uniquePaths(3,7));*/
       // System.out.println(longestPalindrome("abaa"));
        /*System.out.println(longestPalindrome("aaaa"));*/
 /*       System.out.println(
                longestCommonSubsequence("abace", "aace")
        );*/
      //  System.out.println(minDistance("horse", "ros"));
        /*System.out.println(climbStairs(3));*/
        List<List<Integer>> generate = generate(5);
        for (List<Integer> list : generate) {
            System.out.println(list);
        }
    }


    public static int numSquares(int n) {
        int []dp=new int[ n+1];
        for (int i = 1; i <= n; i++) {
            int min =Integer.MAX_VALUE;
            for (int j = 1; j*j <= i; j++) {
                min=Math.min(min,dp[i-j*j]);
            }
            dp[i]=1+min;
        }
        return dp[n];
    }
    public static int rob(int[] nums) {
        int len = nums.length;
        if (len==1){
            return nums[0];
        } else if (len == 2) {
            return Math.max(nums[0],nums[1]);
        }
        int[]dp=new int[len];
        dp[0]=nums[0];
        dp[1]=nums[1];
        for (int i = 2; i < len; i++) {
            if (i==2){
                dp[i]=nums[i]+dp[i-2];
            }else {
                dp[i]=nums[i]+Math.max(dp[i-2],dp[i-3]);
            }
        }
        return Math.max(dp[len-1],dp[len-2]);
    }

    public static List<List<Integer>> generate(int numRows) {
        ArrayList<List<Integer>> ans=new ArrayList<>();
        List<Integer> list = List.of(1);
        ans.add(list);
        if (numRows==1){
            return ans;
        }
        for (int i = 1; i < numRows; i++) {
            List<Integer> tans=new ArrayList<>();
            tans.add(1);
            List<Integer> befor = ans.get(i - 1);
            for (int j = 1; j < befor.size(); j++) {
                tans.add(befor.get(j)+befor.get(j-1));
            }
            tans.add(1);
            ans.add(tans);
        }
        return ans;
    }
    public static int climbStairs(int n) {
        if (n<=2){
            return n;
        }
        int []dp=new int[n];
        dp[0]=1;
        dp[1]=2;
        for (int i = 2; i < n; i++) {
            dp[i]=dp[i-1]+dp[i-2];
        }
        return dp[n-1];
    }

    public static int minDistance(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();

        if (n*m==0){
            return n+m;
        }
        int[][]dp=new int[n+1][m+1];
        for (int i = 1; i <= n; i++) {
            dp[i][0]=i;
        }
        for (int i = 1; i <= m; i++) {
            dp[0][i]=i;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (word1.charAt(i-1)==word2.charAt(j-1)){
                    dp[i][j]=dp[i-1][j-1];
                }else{
                    dp[i][j]=Math.min(dp[i-1][j-1],Math.min(dp[i-1][j],dp[i][j-1]))+1;
                }
            }
        }

        return dp[n][m];
    }

    public static int longestCommonSubsequence(String text1, String text2) {
        int n = text1.length();
        int m = text2.length();
        if (n*m==0){
            return 0;
        }
        int[][]dp=new int[n+1][m+1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (text1.charAt(i-1)==text2.charAt(j-1)){
                    dp[i][j]=dp[i-1][j-1]+1;
                }else {
                    dp[i][j]=Math.max(dp[i-1][j],dp[i][j-1]);
                }
            }
        }
        return dp[n][m];
    }

    public static String longestPalindrome(String s) {
        int start=0;
        int maxlen=1;
        int len = s.length();
        boolean[][]dp=new boolean[len][len];
        for (int i = 0; i < len; i++) {
            dp[i][i]=true;
        }
        for (int l = 1; l <= len; l++) {
            for (int i = 0; i <= len; i++) {
                int end = i + l;
                if (end>=len){
                    continue;
                }
                if (s.charAt(i)==s.charAt(end)){
                    if (l<=2){
                        dp[i][end]=true;
                    }else {
                        dp[i][end]=dp[i+1][end-1];
                    }
                }
                if (dp[i][end]&&l>=maxlen){
                    maxlen=l+1;
                    start=i;
                }
            }
        }
        return s.substring(start,start+maxlen);
    }


    public static int minPathSum(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        int[][]dp=new int[n][m];
        dp[0][0]=grid[0][0];
        for (int i = 1; i < n; i++) {
            dp[i][0]=dp[i-1][0]+grid[i][0];
        }
        for (int i = 1; i < m; i++) {
            dp[0][i]=dp[0][i-1]+grid[0][i];
        }
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                dp[i][j]=grid[i][j]+Math.min(dp[i-1][j],dp[i][j-1]);
            }
        }
        return dp[n-1][m-1];
    }
    public static int uniquePaths(int m, int n) {
        int[][]dp=new int[m][n];
        dp[0][0]=1;
        for (int i = 1; i < m; i++) {
            dp[i][0]=1;
        }
        for (int i = 1; i < n; i++) {
            dp[0][i]=1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j <n; j++) {
                dp[i][j]=dp[i-1][j]+dp[i][j-1];
            }
        }

        return dp[m-1][n-1];
    }

}
