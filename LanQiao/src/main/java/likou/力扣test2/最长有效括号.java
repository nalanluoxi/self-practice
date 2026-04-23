package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：最长有效括号
 * @Date：2025/6/28 12:00
 * @Filename：最长有效括号
 */
public class 最长有效括号 {
    public static void main(String[] args) {
        /*System.out.println(longestValidParentheses(")()())"));
        System.out.println(longestValidParentheses(""));
        System.out.println(longestValidParentheses("(()"));
        System.out.println(longestValidParentheses("())"));
        System.out.println(longestValidParentheses("()(())"));*/
        System.out.println(longestValidParentheses("001"));
    }
    public static int longestValidParentheses(String s) {
        char[] list = s.toCharArray();
        int n = list.length;
        if (n==0||n==1){
            return 0;
        }
        int []dp=new int[n];
        int max=0;
        for (int i = 0; i < n; i++) {
            if (list[i]=='0'){
                dp[i]=0;
            }else {
                if (i-1<0){
                    dp[i]=0;
                    continue;
                }
                int be=dp[i-1];
                if (i-be-1<0||list[i-be-1]=='1'){
                    dp[i]=0;
                    continue;
                }
                dp[i]=2+dp[i-1];
                if (i-be-2>=0){
                    dp[i]+=dp[i-be-2];
                }
            }
            max=Math.max(max,dp[i]);
        }
        return max;
    }
}
