package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：最长回文子序列
 * @Date：2025/4/10 16:18
 * @Filename：最长回文子序列
 */
public class 最长回文子序列 {
    public static void main(String[] args) {
        System.out.println(longestPalindromeSubseq("bbbab"));

    }

    public static int longestPalindromeSubseq(String s) {
        int n = s.length();
        int[][] dp=new int[n][n];
        int leftDown=0,brfor=0;


        return 0;
    }
  /*  static int [][]dp;
    public static int longestPalindromeSubseq(String s) {
        int len = s.length();
        dp=new int[len+1][len+1];
        StringBuffer str = new StringBuffer(s);
        str.reverse();
        String s1 = str.toString();
        return help(s, s1);
    }

    public static int help(String s1, String s2){
        int len = s1.length();
        for (int i = 1; i <= len; i++) {
            for (int j = 1; j <= len; j++) {
                if (s1.charAt(i-1)==s2.charAt(j-1)){
                    dp[i][j]=dp[i-1][j-1]+1;
                }else {
                    dp[i][j]=Math.max(dp[i-1][j],dp[i][j-1]);
                }
            }
        }
        return dp[len][len];
    }*/

   /* static int [][]dp;
    public static int longestPalindromeSubseq(String s) {
        int len = s.length();
        dp=new int[len+1][len+1];
        return help(s,0,s.length()-1);
    }

    public static int help(String s, int l,int r){
        if (l==r){
            return 1;
        }
        if (l+1==r){
            return s.charAt(l)==s.charAt(r)?2:1;
        }
        if (dp[l][r]!=0){
            return dp[l][r];
        }
        int ans=0;
        if (s.charAt(l)==s.charAt(r)){
            ans= help(s,l+1,r-1)+2;
        }else {
            ans= Math.max(help(s,l+1,r),help(s,l,r-1));
        }
        dp[l][r]=ans;
        return ans;
    }*/
   /* public static int longestPalindromeSubseq(String s) {
        StringBuffer str = new StringBuffer(s);
        str.reverse();
        String s1 = str.toString();
        return help(s, s1);
    }

    public static int help(String text1, String text2) {
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
    }*/
   /* static int[][] dp;

    public static int help(String s1, String s2, int l1, int l2) {
        if (l1 == 0 || l2 == 0) {
            return 0;
        }
        if (dp[l1][l2] != 0) {
            return dp[l1][l2];
        }
        int ans = 0;
        if (s1.charAt(l1 - 1) == s2.charAt(l2 - 1)) {
            ans = help(s1, s2, l1 - 1, l2 - 1) + 1;
        } else {
            ans = Math.max(help(s1, s2, l1 - 1, l2), help(s1, s2, l1, l2 - 1));
        }
        dp[l1][l2] = ans;
        return ans;
    }*/
}
