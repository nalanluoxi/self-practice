package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：解码方法2
 * @Date：2025/6/26 11:39
 * @Filename：解码方法2
 */
public class 解码方法2 {
    public static void main(String[] args) {
        System.out.println(numDecodings("226"));
        System.out.println(numDecodings("06"));
        System.out.println(numDecodings("2101"));
    }
    public static int numDecodings(String s) {
        if (s.charAt(0)=='0'){
            return 0;
        }
        int n=s.length();
        int[]dp=new int[n+1];
        dp[0]=1;
        for (int i = 1; i <=n ; i++) {
            if(s.charAt(i-1)=='0'){
                dp[i]=0;
            }else {
                dp[i]=dp[i-1];
            }
            if(i>1&&isOk(s.substring(i-2,i))){
                dp[i]+=dp[i-2];
            }
        }
        return dp[n];
    }

    public static boolean isOk(String s){
        if (s.charAt(0)=='0'){
            return false;
        }
        int num=Integer.parseInt(s);
        return num>=1&&num<=26;
    }
}
