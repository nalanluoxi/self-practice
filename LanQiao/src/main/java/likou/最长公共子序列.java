package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：最长公共子序列
 * @Date：2025/4/3 18:51
 * @Filename：最长公共子序列
 */
public class 最长公共子序列 {


    public static void main(String[] args) {
        System.out.println(longestCommonSubsequence("abcde","ace"));
    }


    public static int longestCommonSubsequence(String text1, String text2) {
        int x = text1.length();
        int y = text2.length();
        if (x*y==0){
            return 0;
        }
        int [][]dp=new int[x+1][y+1];
        for (int i = x-1; i >= 0; i--) {
            for (int j = y-1; j >= 0; j--) {
                if (text1.charAt(i)==text2.charAt(j)){
                    dp[i][j]=dp[i+1][j+1]+1;
                }else {
                    dp[i][j]=Math.max(dp[i+1][j],dp[i][j+1]);
                }
            }
        }
        return dp[0][0];
    }
}
