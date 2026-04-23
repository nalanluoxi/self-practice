package 蓝桥杯真题.省12A组;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.省12A组
 * @Project：LanQiaoBei
 * @name：路径
 * @Date：2025/4/10 22:20
 * @Filename：路径
 */
public class 路径 {
    public static void main(String[] args) {
        int n = 2021;
        System.out.println(lun(n));
        System.out.println("10266837");
    }

    public static long lun(int n) {
        int[] dp = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            dp[i] = Integer.MAX_VALUE;
        }
        dp[1]=0;
        for (int i = 1; i <= n; i++) {
            for (int j = i + 1; j <= n && j <= i + 21; j++) {
                dp[j]=Math.min(dp[j],dp[i]+getbei(i,j));
            }
        }
        return dp[n];
    }

    public static int yue(int a, int b) {
        return b == 0 ? a : yue(b, a % b);
    }

    public static int getbei(int a, int b) {
        int yue = yue(a, b);
        return a * b / yue;
    }
}
