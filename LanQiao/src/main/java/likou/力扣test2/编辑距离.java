package likou.力扣test2;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：编辑距离
 * @Date：2025/6/27 22:01
 * @Filename：编辑距离
 */
public class 编辑距离 {

    public static void main(String[] args) {
        System.out.println(minDistance("horse", "ros"));
        System.out.println(minDistance("intention", "execution"));
    }


    public static int minDistance(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();
        int[][]dp=new int[n+1][m+1];
        for (int i = 0; i <= n; i++) {
            dp[i][0]=i;
        }
        for (int i = 0; i <= m; i++) {
            dp[0][i]=i;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (word1.charAt(i-1)==word2.charAt(j-1)){
                    dp[i][j]=dp[i-1][j-1];
                }else {
                    dp[i][j]=Math.min(Math.min(dp[i-1][j-1],dp[i-1][j]),dp[i][j-1])+1;
                }
            }
        }
        return dp[n][m];
    }

    /*static int [][]dp;
    public static int minDistance(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();
        dp=new int[n][m];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i],-1);
        }
        char[] c1 = word1.toCharArray();
        char[] c2 = word2.toCharArray();
        return dfs(c1,c2,n-1,m-1);
    }

    public static int dfs(char[] c1,char[] c2,int i,int j){
        if (i<0){
            return j+1;
        } else if (j<0) {
            return i+1;
        }
        if (dp[i][j]!=-1){
            return dp[i][j];
        }
        int t;
        if (c1[i]==c2[j]){
            t=dfs(c1,c2,i-1,j-1);
        }else {
            t= Math.min(Math.min(dfs(c1,c2,i-1,j-1),dfs(c1,c2,i-1,j)),dfs(c1,c2,i,j-1))+1;
        }
        dp[i][j]=t;
        return t;
    }
*/
/*    static int[][]dp;
    public static int minDistance(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();
*//*        dp=new int[n][m];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i],-1);
        }*//*
        return n-dfs(word1,word2,n-1,m-1);
    }

    public static int dfs(String word1, String word2, int i, int j) {
        if(i<0||j<0){
            return 0;
        }
        int p1=dfs(word1,word2,i-1,j-1);
        int p2=dfs(word1,word2,i-1,j);
        int p3=dfs(word1,word2,i,j-1);
        int p4=word1.charAt(i)==word2.charAt(j)?p1+1:0;
        return Math.max(Math.max(p1,p4),Math.max(p2,p3));
    }*/

  /*  public static int minDistance(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();
        if(n*m==0){
            return n+m;
        }
        int[][]dp=new int[n+1][m+1];
        for (int i = 0; i < n; i++) {
            dp[i][0]=i;
        }
        for (int i = 0; i < m; i++) {
            dp[0][i]=i;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (word1.charAt(i-1)==word2.charAt(j-1)){
                    dp[i][j]=dp[i-1][j-1];
                }else {
                    dp[i][j]= Math.min(dp[i-1][j-1],Math.min(dp[i][j-1],dp[i-1][j]))+1;
                    //dp[i][j]=Math.min(dp[i-1][j-1],Math.min(dp[i-1][j],dp[i][j-1]))+1;
                }
            }
        }
        return dp[n][m];
    }*/

   /* public static int minDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();
        if (len2*len2==0){
            return len1+len2;
        }
        int[][] dp = new int[len1 + 1][len2 + 1];
       for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        for (int i = 0; i <= len2; i++) {
            dp[0][i] = i;
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
*/
   /* public static int minDistance(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();
        int[][] dp = new int[n + 1][m + 1];  // dp[i][j]表示word1的前i个字符和word2的前j个字符的最小编辑距离

        // 初始化：如果其中一个字符串为空，编辑距离是另一个字符串的长度
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;  // 删除所有字符
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;  // 插入所有字符
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];  // 字符相同，无需操作
                } else {
                    // 取插入、删除、替换中的最小值
                    dp[i][j] = 1 + Math.min(
                            Math.min(dp[i][j - 1],    // 插入
                                    dp[i - 1][j]),   // 删除
                            dp[i - 1][j - 1]          // 替换
                    );
                }
            }
        }
        return dp[n][m];
    }
*/
 /*   static int ans;
    public static int minDistance(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();
        int[][]dp=new int[n][m];
        for (int i = 0; i < dp.length; i++) {
            Arrays.fill(dp[i],-1);
        }
        ans=0;
        dfs(n-1,m-1,word1,word2,dp);
        return ans;
    }
*/
    public static int dfs(int i,int j,String word1,String word2,int[][]dp){
        if(i<0){
            return 1;
        } else if (j<0) {
            return 1;
        }
        if (dp[i][j]!=-1){
            return dp[i][j];
        }
        int t=0;
        if(word1.charAt(i)==word2.charAt(j)){
             t=dfs(i-1,j-1,word1,word2,dp)+1;
        }else {
            t=Math.min(dfs(i-1,j,word1,word2,dp),dfs(i,j-1,word1,word2,dp));
        }
        dp[i][j]=t;

        return t;
    }
}
