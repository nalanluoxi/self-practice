package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：最长有效括号
 * @Date：2025/4/8 19:42
 * @Filename：最长有效括号
 */
public class 最长有效括号 {
    public static void main(String[] args) {
        System.out.println(longestValidParentheses("()"));
    }

    static int[] dp;

    public static int longestValidParentheses(String s) {
        dp = new int[s.length()];
        int ans = 0;
        char[] charArray = s.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (c == '(') {
                dp[i] = 0;
            } else {
                if (i - 1 < 0) {
                    dp[i] = 0;
                    continue;
                }
                int before = dp[i - 1];
                if (i - before - 1 < 0 || charArray[i - before - 1] == ')') {
                    dp[i] = 0;
                } else {
                    dp[i] = dp[i - 1] + 2 ;/*+ dp[i - before - 2];*/
                    if (i - before - 2 >= 0) {
                        dp[i] += dp[i - before - 2];
                    }
                    ans = Math.max(ans, dp[i]);
                }
            }
        }
        return ans;
    }
}
