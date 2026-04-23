package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：最长公共子序列
 * @Date：2025/4/9 21:14
 * @Filename：最长公共子序列
 */
public class 最长公共子序列 {
    public static void main(String[] args) {
        System.out.println(longestCommonSubsequence("abcde", "ace"));
    }

   public static int longestCommonSubsequence(String text1, String text2) {
        char[] s1 = text1.toCharArray();
        char[] s2 = text2.toCharArray();
        dp=new int[s1.length+1][s2.length+1];
        return help(s1, s2, s1.length , s2.length );
    }

    static int [][]dp;
    public static int help(char[] s1, char[] s2, int l1, int l2) {
        if (l1 == 0 || l2 == 0) {
            return 0;
        }
        if (dp[l1][l2]!=0){
            return dp[l1][l2];
        }
        int ans;
        if (s1[l1-1] == s2[l2-1]) {
            ans = help(s1, s2, l1 - 1, l2 - 1) + 1;
        } else {
            ans = Math.max(help(s1, s2, l1 - 1, l2), help(s1, s2, l1, l2 - 1));
        }
        dp[l1][l2]=ans;
        return ans;
    }
    public static int longestCommonSubsequence2(String text1, String text2) {
        char[] s1;
        char[] s2;
        if (text1.length() > text2.length()) {
            s1 = text1.toCharArray();
            s2 = text2.toCharArray();
        }else {
            s1 = text2.toCharArray();
            s2 = text1.toCharArray();
        }
        int n = s1.length;
        int m = s2.length;
        int [] dp=new int[m+1];
        for (int i = 1; i <= n; i++) {
            int leftup = 0,back;
            for (int j = 1; j <= m; j++) {
                back = dp[j];
                if (s1[i-1]==s2[j-1]){
                    dp[j]=leftup+1;
                }else {
                    dp[j]=Math.max(dp[j-1],dp[j]);
                }
                leftup=back;
            }
        }
        return dp[m];
    }

    //static int [][]dp;

}
