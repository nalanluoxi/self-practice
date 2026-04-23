package likou.力扣test2;

import PTA.舍入;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：最长回文子串
 * @Date：2025/6/6 11:40
 * @Filename：最长回文子串
 */
public class 最长回文子串 {
    public static void main(String[] args) {

       // System.out.println(longestPalindrome2("bb"));
        System.out.println(longestPalindrome("bb"));
    }
    public static String longestPalindrome(String s) {
        int len=s.length();
        boolean [][] dp=new boolean[len][len];
        for(int i=0;i<len;i++){
            dp[i][i]=true;
        }
        int start=0;
        int maxlen=1;
        for (int le=2;le<=len;le++){
            for(int j=0;j<len;j++){
                int end=j+le-1;
                if(end>=len){
                    break;
                }
                if(s.charAt(j)!=s.charAt(end)){
                    dp[j][end]=false;
                }else{
                    if(end-j<3){
                        dp[j][end]=true;
                    }else{
                        dp[j][end]=dp[j+1][end-1];
                    }
                }
                if(dp[j][end]&&end-j+1>maxlen){
                    maxlen=end-j+1;
                    start=j;
                }
            }
        }
        return s.substring(start,start+maxlen);
    }
//    public static String longestPalindrome2(String s) {
//        if (s.length()==0||s.equals("")){
//            return "";
//        }
//        boolean [][]dp=new boolean[s.length()][s.length()];
//        int start =0;
//        int maxlen=1;
//        for (int i = 0; i < dp.length; i++) {
//            dp[i][i]=true;
//        }
//        for (int l = maxlen; l <= s.length(); l++) {
//            for (int i = 0; i < s.length(); i++) {
//                int j=i+l-1;
//                if (j>=s.length()){
//                    break;
//                }
//                if (s.charAt(i)!=s.charAt(j)){
//                    dp[i][j]=false;
//                }else {
//                    if (j-i<3){
//                        dp[i][j]=true;
//                    }else {
//                        dp[i][j]=dp[i+1][j-1];
//                    }
//                }
//
//                if (dp[i][j] && j-i+1>maxlen){
//                    //maxlen=j-i+1;
//                    start=i;
//                    maxlen=j-i+1;
//                }
//            }
//        }
//        return s.substring(start,start+maxlen);
//    }
//
//
//    public static String longestPalindrome(String s) {
//        int len = s.length();
//        boolean [][]dp=new boolean[len][len];
//
//
//        for (int i = dp.length - 1; i >= 0; i--) {
//            dp[i][i]=true;
//        }
//        int start=0;
//        int maxLen=1;
//        for (int l=maxLen;l<=len;l++){
//            for (int i = 0; i < len; i++) {
//                int j=i+l-1;
//                if (j>=len){
//                    break;
//                }
//                if (s.charAt(i)!=s.charAt(j)){
//                    dp[i][j]=false;
//                }else {
//                    if (j-i<3){
//                        dp[i][j]=true;
//                    }else {
//                        dp[i][j]=dp[i+1][j-1];
//                    }
//                }
//
//                if (dp[i][j] && j-i+1>maxLen){
//                    start=i;
//                    maxLen=j-i+1;
//                }
//            }
//        }
//        return s.substring(start,start+maxLen);
//    }
//    public static boolean isHui(String str){
//        StringBuilder sb =new StringBuilder(str);
//        return sb.reverse().toString().equals(str);
//    }

}
