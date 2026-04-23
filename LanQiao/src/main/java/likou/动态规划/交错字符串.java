package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：交错字符串
 * @Date：2025/4/10 19:55
 * @Filename：交错字符串
 */
public class 交错字符串 {
    public static void main(String[] args) {
        System.out.println(isInterleave("aabcc", "dbbca", "aadbbcbcac"));
        //System.out.println(isInterleave("aabc", "abad", "aabadabc"));
       // System.out.println(isInterleave("db","b","cbb"));
    }

    public static boolean isInterleave(String s1, String s2, String tar) {
        int n = s1.length();
        int m = s2.length();
        int t = tar.length();
        if (n + m != t) {
            return false;
        }
        return help(s1, n, s2, m, tar, t);
    }

    public static boolean help(String s1, int index1, String s2, int index2, String tar, int index3) {
        if (index3 == 0 && index1 == 0 && index2 == 0) {
            return true;
        } else if (index3 == 0 || index1 == 0 || index2 == 0) {
            return false;
        }

        boolean ans;
        if( (index1 != 0 && s1.charAt(index1 - 1) == tar.charAt(index3 - 1))||(index2 != 0 && s2.charAt(index2 - 1) == tar.charAt(index3 - 1)) ) {
            ans = help(s1, index1 - 1, s2, index2, tar, index3 - 1)||help(s1, index1, s2, index2 - 1, tar, index3 - 1);
        }else {
            ans = false;
        }
        return ans;
    }
}
