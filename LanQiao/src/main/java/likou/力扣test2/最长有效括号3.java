package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：最长有效括号3
 * @Date：2025/7/1 19:12
 * @Filename：最长有效括号3
 */
public class 最长有效括号3 {

    public static void main(String[] args) {
      System.out.println(longestValidParentheses(")()())"));
        System.out.println(longestValidParentheses("(()"));
        System.out.println(longestValidParentheses("()()))))()()("));
    }


    public static int longestValidParentheses(String s) {
        int len = s.length();
        if (len==0||len==1){
            return 0;
        }
        int ans=0;
        int[]dp=new int[len];
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i)=='('){
                dp[i]=0;
            }else if (s.charAt(i)==')'){
                if (i==0){
                    dp[i]=0;
                    continue;
                }
                int befor = dp[i - 1];
                if (i-befor-1<0|| s.charAt(i-befor-1)==')'){
                    dp[i]=0;
                } else {
                    dp[i]+=dp[i-1]+2;
                    if (i-befor-2>=0){
                        dp[i]+=dp[i-befor-2];
                    }
                    ans=Math.max(ans,dp[i]);
                }

            }

        }
        return ans;
    }
}
