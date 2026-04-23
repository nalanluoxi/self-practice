package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：最长回文子串
 * @Date：2025/3/21 9:31
 * @Filename：最长回文子串
 */
public class 最长回文子串 {
    public static void main(String[] args) {
       /* String string = longestPalindrome("babad");
        System.out.println(string);*/
        //System.out.println(longestPalindrome("cbbd"));
        System.out.println(longestPalindrome("ac"));
        //System.out.println(longestPalindrome("aa"));
    }


    public static String longestPalindrome(String s) {
        int len = s.length();
        if (len<2)return s;
        int start=0,maxlen=1;
        boolean[][] dp=new boolean[len][len];
        for (int i = 0; i < len; i++) dp[i][i]=true;
        for (int l = 2; l <= len; l++) {
            for (int i = 0; i < len; i++) {
                int j=i+l-1;
                if (j >= len) break;
                if (s.charAt(i)!=s.charAt(j)){
                    dp[i][j]=false;
                }else {
                    if (j-i<3){
                        dp[i][j]=true;
                    }else {
                        dp[i][j]=dp[i+1][j-1];
                    }
                }
                if (dp[i][j] && j-i+1>maxlen){
                    maxlen=j-i+1;
                    start=i;
                }
            }
        }
        return s.substring(start,start+maxlen);
    }
    /*public static String longestPalindrome(String s) {
        String ans="";
        for (int i = 0; i < s.length(); i++) {
            for (int j = s.length()-1; j >=0  && (j-i)>=ans.length(); j--) {
                String substring = s.substring(i, j+1);
                if (isHu(substring)){
                    ans=substring;
                }
            }
        }
        return ans;
    }
    public static boolean isHu(String  s){
        StringBuilder st=new StringBuilder(s);
        String s2 = st.reverse().toString();
        if (s.equals(s2)){
            return true;
        }
        return false;
    }*/
}
