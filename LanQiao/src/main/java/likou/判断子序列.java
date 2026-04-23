package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：判断子序列
 * @Date：2025/2/2 17:35
 * @Filename：判断子序列
 */
public class 判断子序列 {
    public static void main(String[] args) {
        boolean subsequence = isSubsequence("abc", "ahbgdc");
        System.out.println(subsequence);
    }
    public static boolean isSubsequence(String s, String t) {
        int l=0;
        int r=0;
        while (l<s.length()&&r<t.length()){
            if (s.charAt(l)==t.charAt(r)){
                l++;
                r++;
            }else {
                r++;
            }
        }
        if (l==s.length()&&r<=t.length()){
            return true;
        }
        return false;
    }
}
