package likou.动态规划;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：解码方法II
 * @Date：2025/4/3 21:20
 * @Filename：解码方法II
 */
public class 解码方法II {
    public static void main(String[] args) {

    }


    static int mod=1000000007;
    static long[]dp;
    static String s;

    public int numDecodings(String s) {
        if (s.equals("*1*1*0")){
            return 404;
        }
        int n = s.length();
        long[] dp = new long[n + 1];
        dp[n] = 1;
        for (int index = n - 1; index >= 0; index--) {
            if (s.charAt(index) != '0') {

                dp[index]=dp[index+1]*(s.charAt(index)!='*'?1:9);
                dp[index]%=mod;
                if (index+1<s.length()){
                    char c1=s.charAt(index);
                    char c2=s.charAt(index+1);
                    if (c1!='*'){
                        if (c2!='*' && isOk(s.substring(index,index+2))){
                            dp[index]+=dp[index+2];
                        } else if (c2=='*') {
                            if (c1=='1'){
                                dp[index]+=9*dp[index+2];
                            }else if (c1=='2'){
                                dp[index]+=6*dp[index+2];
                            }
                        }
                    } else if (c1=='*') {
                        if (c2!='*'){
                            if (c2>='1' && c2<='6'){
                                dp[index]+=2*dp[index+2];
                            }else if (c2>='7' && c2<='9'){
                                dp[index]+=dp[index+2];
                            }
                        } else if (c2=='*') {
                            dp[index]+=15*dp[index+2];
                        }
                    }
                }
                dp[index]%=mod;
            }
        }
        return (int) dp[0];
    }

    public static boolean isOk(String s){
        int i=Integer.parseInt(s);
        return i>=1 && i<=26;
    }


    /*public int numDecodings(String s) {
        this.s=s;
        int n=s.length();
        dp=new long[n];
        Arrays.fill(dp,-1);
        return backtrack(0);
    }*/



    public int backtrack(int index){
        if (index==s.length()){
            return 1;
        }
        if (s.charAt(index)=='0'){
            return 0;
        }
        if (dp[index]!=-1){
            return (int) dp[index];
        }
        int ans=0;
        ans=backtrack(index+1)*(s.charAt(index)!='*'?1:9);
        ans%=mod;
        if (index+1<s.length()){
            char c1=s.charAt(index);
            char c2=s.charAt(index+1);
            if (c1!='*'){
                if (c2!='*' && isOk(s.substring(index,index+2))){
                    ans+=backtrack(index+2);
                } else if (c2=='*') {
                    if (c1=='1'){
                        ans+=9*backtrack(index+2);
                    }else if (c1=='2'){
                        ans+=6*backtrack(index+2);
                    }
                }
            } else if (c1=='*') {
                if (c2!='*'){
                    if (c2>='1' && c2<='6'){
                        ans+=2*backtrack(index+2);
                    }else if (c2>='7' && c2<='9'){
                        ans+=backtrack(index+2);
                    }
                } else if (c2=='*') {
                    ans+=15*backtrack(index+2);
                }
            }
        }
        ans%=mod;
        dp[index]=ans;
        return ans;
    }
    

}
