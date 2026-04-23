package likou.动态规划;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：解码方法
 * @Date：2025/4/3 19:04
 * @Filename：解码方法
 */
public class 解码方法 {
    public static void main(String[] args) {
        int i = numDecodings("123");
        System.out.println(i);
    }

    static int[]dp;
    static String s;
    public static int numDecodings(String string) {
        int n=string.length();
        dp=new int[n+1];

        s=string;
        Arrays.fill(dp,-1);
        dp[n]=1;
        return help();
    }

    public static int help(){
        for (int i = s.length()-1; i >= 0; i--) {
            if (s.charAt(i)=='0'){
                dp[i]=0;
            }else {
                dp[i]=dp[i+1];
                if (i+1<s.length() && isOk(s.substring(i,i+2))){
                    dp[i]+=dp[i+2];
                }
            }
        }
        return dp[0];
    }


   /* static String s;
    public static int numDecodings(String string) {
        s=string;
        return dp(0);

    }

    public static int dp(int index){
        if (index==s.length()){
            return 1;
        }
        int ans=0;
        if (s.charAt(index)=='0'){
            return 0;
        }else {
            ans=dp(index+1);
            if (index+1<s.length() && isOk(s.substring(index,index+2))){
                ans+=dp(index+2);
            }
        }
        return ans;
    }*/

    public static boolean isOk(String string){
        if (string.length()<1){
            return false;
        }
        if (string.charAt(0)=='0'){
            return false;
        }
        int num=Integer.parseInt(string);
        if (num>26){
            return false;
        }
        return true;
    }
}
