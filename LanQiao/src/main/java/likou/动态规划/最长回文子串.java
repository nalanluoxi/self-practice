package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：最长回文子串
 * @Date：2025/6/10 9:47
 * @Filename：最长回文子串
 */
public class 最长回文子串 {
    public static void main(String[] args) {
        System.out.println(longestPalindrome("babad"));
    }
    public static String longestPalindrome(String s) {
        int len=s.length();
        boolean[][]dp=new boolean[len][len];
        for(int i=0;i<len;i++){
            dp[i][i]=true;
        }
        int start=0;
        int maxlen=1;
        for(int le=1;le<=len;le++){
            for(int i=0;i<len;i++){
                int j=i+le-1;
                if(j>=len){
                    continue;
                }
                if(s.charAt(i)==s.charAt(j) ){
                    if(j-i<3){
                        dp[i][j]=true;
                    }else{
                        dp[i][j]=dp[i+1][j-1];
                    }
                }else{
                    continue;
                }
                if(dp[i][j]&&j-i+1>maxlen){
                    start=i;
                    maxlen=j-i+1;
                }
            }
        }
        return s.substring(start,start+maxlen);
    }
}
