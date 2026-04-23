package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：最长回文子串3
 * @Date：2025/6/26 17:38
 * @Filename：最长回文子串3
 */
public class 最长回文子串3 {

    public static void main(String[] args) {
        System.out.println(longestPalindrome("aacabdkacaa"));
    }
    public static String longestPalindrome(String s) {
        int start =0;
        int maxlen=1;
        int n=s.length();
        boolean [][]dp=new boolean[n][n];
        for (int i = 0; i < n; i++) {
            dp[i][i]=true;
        }
        for (int l = 1; l <= n; l++) {
            for (int i = 0; i <= n; i++) {
                int st=i;
                int end=i+l;
                if (end>=n){
                    continue;
                }
                if (s.charAt(st)==s.charAt(end)){
                    if (end-st<3){
                        dp[st][end]=true;
                    }else {
                        dp[st][end]=dp[st+1][end-1];
                    }
                }else {
                    dp[st][end]=false;
                }
                if (dp[st][end] && end-st+1>=maxlen){
                    maxlen=end-st+1;
                    start=st;
                }
            }
        }
        return s.substring(start,start+maxlen);
    }
}
