package likou;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：单词拆分2
 * @Date：2025/5/18 10:20
 * @Filename：单词拆分2
 */
public class 单词拆分2 {
    public static void main(String[] args) {
       // System.out.println(wordBreak("catsanddog", List.of("cats", "dog", "sand", "and", "cat")));
        System.out.println(wordBreak("leetcode", List.of("leet", "code")));
    }

    static int[] dp;
    static Set<String> set;
    static int maxlen;

    public static boolean wordBreak(String s, List<String> wordDict) {
        set = new HashSet<>(wordDict);
        for (String string : set) {
            maxlen = Math.max(maxlen, string.length());
        }
        dp = new int[s.length() + 1];
        Arrays.fill(dp, -1);
        return dfs(s, 0) == 1;
    }

    public static int dfs(String s, int i) {
        if (i == s.length()) {
            return 1;
        }
        if (dp[i] !=  -1) {
            return dp[i];
        }
        for (int j = i + 1; j < Math.min(s.length(),j+maxlen)+1; j++) {
            if (set.contains(s.substring(i, j)) && dfs(s, j)==1){
                return dp[i]=1;
            }
        }

        return dp[i]=0;
    }
}
