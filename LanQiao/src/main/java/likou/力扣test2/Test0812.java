package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2.前缀树
 * @Project：LanQiaoBei
 * @name：Test0812
 * @Date：2025/8/12 22:20
 * @Filename：Test0812
 */
public class Test0812 {

    public static void main(String[] args) {
        //   System.out.println(longestPalindrome("babad"));
        //  System.out.println(longestPalindrome("aacabdkacaa"));
        //System.out.println(lengthOfLIS(new int[]{0}));

        //System.out.println(longestCommonSubsequence("abcde", "ace"));
        /*System.out.println(minDistance("horse","ros"));*/
       // System.out.println(longestValidParentheses("(()"));
       // System.out.println(longestValidParentheses(")()())"));
        System.out.println(longestValidParentheses("()()))))()()("));
    }

    public static int longestValidParentheses(String s) {
        int []dp=new int[s.length()];
        int ans=0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i)=='('){
                continue;
            } else if (s.charAt(i)==')'&&i==0) {
                continue;
            } else if (s.charAt(i) == ')') {
                int befor = dp[i - 1];
                if (i-befor-1>=0 && s.charAt(i-befor-1)=='('){
                    dp[i]=befor+2;
                    if (i-befor-2>=0){
                        dp[i]+=dp[i-befor-2];
                    }
                }

            }
            ans=Math.max(ans,dp[i]);
        }
        return ans;
    }

    public static int minDistance(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();
        if (n*m==0){
            return n+m;
        }
        int[][]dp=new int[n+1][m+1];
        for (int i = 0; i <= n; i++) {
            dp[i][0]=i;
        }
        for (int i = 0; i <= m; i++) {
            dp[0][i]=i;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (word1.charAt(i-1)==word2.charAt(j-1)){
                    dp[i][j]=dp[i-1][j-1];
                }else {
                    dp[i][j]=Math.min(dp[i-1][j-1],Math.min(dp[i-1][j],dp[i][j-1]))+1;
                }
            }
        }
        return dp[n][m];
    }

    public static int longestCommonSubsequence(String text1, String text2) {
        int n = text1.length();
        int m = text2.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[n][m];
    }

    public static int longestCommonSubsequence1(String text1, String text2) {
        return help(text1, text1.length() - 1, text2, text2.length() - 1);
    }

    public static int help(String s1, int i, String s2, int j) {
        if (i < 0 || j < 0) {
            return 0;
        }
        if (s1.charAt(i) == s2.charAt(j)) {
            return help(s1, i - 1, s2, j - 1) + 1;
        } else {
            return Math.max(help(s1, i, s2, j - 1), help(s1, i - 1, s2, j));
        }
    }

    public static int lengthOfLIS(int[] nums) {
        int len = nums.length;
        int[] dp = new int[len];
        dp[len - 1] = 1;
        int ans = 1;
        for (int i = len - 2; i >= 0; i--) {
            dp[i] = 1;
            for (int j = i + 1; j < len; j++) {
                if (nums[j] > nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            ans = Math.max(ans, dp[i]);
        }
        return ans;
    }


    public static String longestPalindrome(String s) {
        int start = 0;
        int maxlen = 1;
        int len = s.length();
        boolean[][] dp = new boolean[len][len];
        for (int i = 0; i < len; i++) {
            dp[i][i] = true;
        }
        for (int le = 1; le <= len; le++) {
            for (int i = 0; i <= len; i++) {
                int end = i + le;
                if (end >= len) {
                    continue;
                }
                if (s.charAt(i) == s.charAt(end)) {
                    if (le == 1) {
                        dp[i][end] = true;
                    } else {
                        dp[i][end] = dp[i + 1][end - 1];
                    }
                } else {
                    dp[i][end] = false;
                }
                if (dp[i][end] && le + 1 > maxlen) {
                    start = i;
                    maxlen = le + 1;
                }
            }
        }
        return s.substring(start, start + maxlen);
    }

    public static int maxSubArray1(int[] nums) {
        int ans = nums[0];
        int sum = 0;
        for (int num : nums) {
            sum = Math.max(num, num + sum);
            ans = Math.max(ans, sum);
        }
        return ans;
    }

    public static int maxSubArray(int[] nums) {
        int ans = nums[0];
        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            dp[i] = Math.max(nums[i], nums[i] + dp[i - 1]);
            ans = Math.max(ans, dp[i]);
        }
        return ans;
    }


    public int maxAreaOfIsland(int[][] nums) {
        int n = nums.length;
        int m = nums[0].length;
        int max = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (nums[i][j] == 1) {
                    int size = help(i, j, nums);
                    max = Math.max(max, size);
                }
            }
        }
        return max;
    }

    public int help(int i, int j, int[][] nums) {
        int n = nums.length;
        int m = nums[0].length;
        if (i < 0 || i >= n || j < 0 || j >= m || nums[i][j] == 0) {
            return 0;
        }
        nums[i][j] = 0;
        int s1 = help(i + 1, j, nums);
        int s2 = help(i - 1, j, nums);
        int s3 = help(i, j + 1, nums);
        int s4 = help(i, j - 1, nums);
        return 1 + s1 + s2 + s3 + s4;
    }


}
