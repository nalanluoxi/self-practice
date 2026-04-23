package likou.力扣test2;

import ch.qos.logback.core.pattern.color.ANSIConstants;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Test0125 {

    public static void main(String[] args) {
        /*int[]nums={0,1,0,2,1,0,1,3,2,1,2,1};
        System.out.println(trap(nums));*/
        //System.out.println(longestCommonSubsequence("abcde","ace"));

        //System.out.println(longestValidParentheses("()(())"));


       /* System.out.println(minPathSum(new int[][]{
                {1,3,1},
                {1,5,1},
                {4,2,1}
        }));*/
        /*System.out.println(maximalSquare(new char[][]{
                {'1','0','1','0','0'},
                {'1','0','1','1','1'},
                {'1','1','1','1','1'},
                {'1','0','0','1','0'}
        }));*/

        System.out.println(maxProfit(new int[]{2,3,-2,4}));

    }




/*    public boolean wordBreak2(String s, List<String> wordDict) {
        int[]dp=new int[s.length()];
        Arrays.fill(dp,-1);
        int maxlen=0;
        for (String string : wordDict) {
            maxlen=Math.max(maxlen,string.length());
        }
        return dfs(s,wordDict,0,maxlen,dp)==1;
    }

    public static int dfs(String s,List<String> words,int start,int maxlen,int []dp){
        if (start==s.length()){
            return 1;
        }
        if (dp[start]!=-1){
            return dp[start];
        }
        for (int i = start+1; i <s.length(); i++) {
            if (words.contains(s.substring(start,i))&&dfs(s,words,i,maxlen,dp)==1){
                return dp[start]=1;
            }
        }
        return dp[start]=0;
    }*/
    public static boolean wordBreak(String s, List<String> wordDict) {
        int len = s.length();
        int[]dp=new int[len];
        Arrays.fill(dp,-1);
        return dfs(s,wordDict,0,dp)==1;
    }

    public static int dfs(String s,List<String> words,int i,int[]dp){
        if (i==s.length()){
            return 1;
        }
        if (dp[i]!=-1){
            return dp[i];
        }
        for (int j = i+1; j < s.length(); j++) {
            if (words.contains(s.substring(i,j)) && dfs(s,words,j,dp)==1){
                return dp[i]=1;
            }
        }
        return dp[i]=0;
    }
    public boolean wordBreak3(String s, List<String> wordDict) {
        int[]dp=new int[s.length()];
        Arrays.fill(dp,-1);
        int maxlen=0;
        for (String string : wordDict) {
            maxlen=Math.max(maxlen,string.length());
        }
        return dfs(s,wordDict,0,maxlen,dp)==1;
    }

    public static int dfs(String s,List<String> words,int start,int maxlen,int []dp){
        if (start==s.length()){
            return 1;
        }
        if (dp[start]!=-1){
            return dp[start];
        }
        for (int i = start+1; i < s.length()+1; i++) {
            if (words.contains(s.substring(start,i))&&dfs(s,words,i,maxlen,dp)==1){
                return dp[start]=1;
            }
        }
        return dp[start]=0;
    }


    public static int rob(int[] nums) {
        int len = nums.length;
        if (len==0){
            return 0;
        } else if (len==1) {
            return nums[0];
        } else if (len == 2) {
            return Math.max(nums[0],nums[1]);
        }
        int[]dp=new int[len];
        dp[0]=nums[0];
        dp[1]=nums[1];
        for (int i = 2; i < len; i++) {
            if (i-3>=0){
                dp[i]=Math.max(dp[i-2],dp[i-3])+nums[i];
            }else {
                dp[i]=nums[i]+dp[i-2];
            }
        }

        return Math.max(dp[len-1],dp[len-2]);
    }



    public static int uniquePaths(int m, int n) {
        int[][]dp=new int[n][m];
        for (int i = 0; i < n; i++) {
            dp[i][0]=1;
        }
        for (int i = 1; i < m; i++) {
            dp[0][i]=1;
        }
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                dp[i][j]=dp[i-1][j]+dp[i][j-1];
            }
        }
        return dp[n-1][m-1];
    }

    public static int maxProduct(int[] nums) {
        int len = nums.length;
        int [][]dp=new int[len][2];
        int ans=nums[0];
        dp[0][0]=nums[0];
        dp[0][1]=nums[0];
        for (int i = 1; i < len; i++) {
            dp[i][0]=Math.max(nums[i]*dp[i-1][0],Math.max(nums[i]*dp[i-1][1],nums[i]));
            dp[i][1]=Math.min(nums[i]*dp[i-1][0],Math.min(nums[i]*dp[i-1][1],nums[i]));
            ans=Math.max(ans,dp[i][0]);
        }
        return ans;
    }


    public static int maximalSquare(char[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;
        int[][]dp=new int[n][m];
        int ans=0;
        for (int i = 0; i < n; i++) {
            dp[i][0]=matrix[i][0]-'0';
            ans=Math.max(ans,dp[i][0]);
        }
        for (int i = 0; i < m; i++) {
            dp[0][i]=matrix[0][i]-'0';
            ans=Math.max(ans,dp[0][i]);
        }

        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                if (matrix[i][j]=='0'){
                    continue;
                }
                dp[i][j]=Math.min(dp[i-1][j-1],Math.min(dp[i-1][j],dp[i][j-1]))+1;
                ans=Math.max(ans,dp[i][j]);
            }

        }

        return ans*ans;
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



    public static int coinChange(int[] coins, int amount) {
        int[]dp=new int[amount+1];
        Arrays.sort(coins);

        for (int i = 1; i <= amount; i++) {
            dp[i]=Integer.MAX_VALUE;
            for (int coin : coins) {
                if (i-coin>=0 && dp[i-coin]!=Integer.MAX_VALUE){
                    dp[i]=Math.min(dp[i],dp[i-coin]+1);
                }
            }
        }

        return dp[amount]==Integer.MAX_VALUE?-1:dp[amount];
    }


    public static int climbStairs(int n) {
        if (n<=3){
            return n;
        }
        int[]dp=new int[n];
        dp[0]=1;
        dp[1]=2;
        for (int i = 2; i < n; i++) {
            dp[i]=dp[i-1]+dp[i-2];
        }
        return dp[n-1];
    }


    public static int longestValidParentheses(String s) {
        if (s.length()<=1){
            return 0;
        }
        int len = s.length();
        int[]dp=new int[len];
        int ans=0;
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c=='('||i==0){
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

    public static int longestCommonSubsequence(String text1, String text2) {
        int len1 = text1.length();
        int len2 = text2.length();
        if (len1*len2==0){
            return 0;
        }
        int[][]dp=new int[len1+1][len2+1];
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (text1.charAt(i-1)==text2.charAt(j-1)){
                    dp[i][j]=dp[i-1][j-1]+1;
                }else {
                    dp[i][j]=Math.max(dp[i-1][j-1],Math.max(dp[i-1][j],dp[i][j-1]));
                }
            }
        }
        return dp[len1][len2];
    }


    public static int trap(int[] height) {
        int len = height.length;
        Deque<Integer> deque=new LinkedList<>();
        int ans=0;
        for (int i = 0; i < len; i++) {
            while (!deque.isEmpty() && height[deque.peekLast()]<height[i]){
                Integer but = deque.pollLast();
                if (!deque.isEmpty()){
                    Integer left = deque.peekLast();
                    int wei = i - left-1;
                    int hei = Math.min(height[i],height[left]) - height[but];
                    ans+=wei*hei;
                }
            }
            deque.addLast(i);
        }

        return ans;
    }

    public static int minDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();
        if (len1*len2==0){
            return len1+len2;
        }

        int[][]dp=new int[len1+1][len2+1];
        for (int i = 1; i <= len1; i++) {
            dp[i][0]=i;
        }
        for (int i = 1; i <= len2; i++) {
            dp[0][i]=i;
        }

        for (int i = 1; i <= len1; i++) {
            for (int j = 0; j <= len2; j++) {
                if (word1.charAt(i-1)==word2.charAt(j-1)){
                    dp[i][j]=dp[i-1][j-1];
                }else {
                    dp[i][j]=Math.min(dp[i-1][j-1],Math.min(dp[i-1][j],dp[i][j-1]))+1;
                }
            }
        }
        return dp[len1][len2];
    }


    public static int lengthOfLIS(int[] nums) {
        int len = nums.length;
        if (len <= 1) {
            return len;
        }
        int ans = 1;
        int[] dp = new int[len];
        Arrays.fill(dp, 1);
        for (int i = len - 2; i >= 0; i--) {
            for (int j = i+1; j <len ; j++) {
                if (nums[i]<nums[j]){
                    dp[i]=Math.max(dp[i],dp[j]+1);
                }
            }
            ans=Math.max(ans,dp[i]);
        }
        return ans;
    }

    public static int maxProfit(int[] prices) {
        int len = prices.length;
        int max = prices[len - 1];
        int ans = 0;
        for (int i = len - 2; i >= 0; i--) {
            int t = max - prices[i];
            ans = Math.max(ans, t);
            max = Math.max(max, prices[i]);
        }


        return ans;
    }
}
