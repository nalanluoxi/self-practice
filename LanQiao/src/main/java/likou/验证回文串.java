package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：验证回文串
 * @Date：2025/1/21 18:22
 * @Filename：验证回文串
 */
public class 验证回文串 {
    public static void main(String[] args) {
        boolean palindrome = isPalindrome("A man, a plan, a canal: Panama");
        System.out.println(palindrome);
    }
    public static boolean isPalindrome(String s) {
        s = s.replaceAll("[^a-zA-Z0-9]", "");
        s=s.toLowerCase();
        StringBuilder reversed = new StringBuilder(s).reverse();
        if (s.equals(reversed.toString())){
            return true;
        }else {
            return false;
        }
    }

    /*public static boolean isPalindrome(String s) {
        //s = s.replaceAll("[^a-zA-Z0-9]", "");
        String string = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c>='a'&&c<='z' || c>='A'&&c<='Z' || c>='0'&&c<='9'){
                if (c>='A'&&c<='Z'){
                    string+=(char)(c+32);
                }else {
                    string+=c;
                }
            }
        }
        StringBuilder reversed = new StringBuilder(string).reverse();
        if (string.equals(reversed.toString())){
            return true;
        }else {
            return false;
        }
    }*/
}
