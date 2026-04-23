package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：不同的子序列2
 * @Date：2025/4/9 17:23
 * @Filename：不同的子序列2
 */
public class 不同的子序列2 {
    public static void main(String[] args) {
       // System.out.println(distinctSubseqII("abc"));
        //System.out.println(distinctSubseqII("aba"));
        System.out.println(distinctSubseqII("aaa"));
    }


    static long[]dp;
    static long mod=1000000007l;
    public static int distinctSubseqII(String s) {
        dp=new long[26];
        char[] charArray = s.toCharArray();
        long ans=1 ,newAdd;
        for (int i = 0; i < charArray.length; i++) {
            char x = charArray[i];
            newAdd=(ans  - dp[x-'a']+mod)%mod;
            ans=(ans+newAdd)%mod;
            dp[x-'a']=(dp[x-'a']+newAdd)%mod;
        }
        return (int) ((ans-1+mod)%mod);
    }
}
