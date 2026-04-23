package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：不同的二叉搜索树
 * @Date：2025/3/11 20:22
 * @Filename：不同的二叉搜索树
 */
public class 不同的二叉搜索树 {
    public static void main(String[] args) {
        System.out.println(numTrees(4));
    }


    static int[] dp;
    public static int numTrees(int n) {
        dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                dp[i] += dp[j - 1] * dp[i - j];
            }
        }
        return dp[n];
    }
}
