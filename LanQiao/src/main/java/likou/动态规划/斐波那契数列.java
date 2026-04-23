package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：斐波那契数列
 * @Date：2025/7/12 10:47
 * @Filename：斐波那契数列
 */
public class 斐波那契数列 {
    public static void main(String[] args) {

    }

    public int fib(int n) {

        if (n==0){
            return 0;
        } else if (n <= 2) {
            return 1;
        }
        long mod=1000000007;
        int []dp=new int[n+1];
        dp[0]=0;
        dp[1]=1;
        dp[2]=1;
        for (int i = 3; i <= n; i++) {
            int t=dp[i-1]+dp[i-2];
            t= (int) (t%mod);
            dp[i]=t;
        }
        return dp[n];
    }
}
