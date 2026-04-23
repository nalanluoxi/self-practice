package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：验证回文串
 * @Date：2025/7/8 15:32
 * @Filename：验证回文串
 */
public class 验证回文串 {

    public static void main(String[] args) {
       // System.out.println(isPalindrome("A man, a plan, a canal: Panama"));
        System.out.println(isPalindrome("0P"));
    }



    public static boolean isPalindrome(String s) {
        s = s.toLowerCase();
        s=s.replaceAll("[^a-z0-9]", "");
        StringBuilder str=new StringBuilder(s);
        String s2 = str.reverse().toString();
        if (s==""||s.isEmpty()){
            return true;
        }
        return s.equals(s2);
    }
}
