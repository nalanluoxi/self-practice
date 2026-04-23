package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：最长公共子序列2
 * @Date：2025/6/28 11:34
 * @Filename：最长公共子序列2
 */
public class 最长公共子序列2 {

    public static void main(String[] args) {
        System.out.println(longestCommonSubsequence("abcde","ace"));
        System.out.println(longestCommonSubsequence("intention","execution"));
    }

    public static int longestCommonSubsequence(String text1, String text2) {
        char[] s1 = text1.toCharArray();
        char[] s2 = text2.toCharArray();
        int m = s1.length;
        int n = s2.length;
        return dfs(s1,s2,m,n);
    }

    public static int dfs(char[] s1, char[] s2, int i, int j) {
        if (i==0||j==0){
            return 0;
        }
        if (s1[i-1]==s2[j-1]){
            return dfs(s1,s2,i-1,j-1)+1;
        }else {
            return Math.max(dfs(s1,s2,i-1,j),dfs(s1,s2,i,j-1));
        }
    }
}
