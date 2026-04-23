package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：变换问题
 * @Date：2024/12/30 13:01
 * @Filename：变换问题
 */
public class 变换问题 {


    public static void main(String[] args) {
        System.out.println(solution("abc", 2).equals("caababbcbcca"));
        //System.out.println(solution("abca", 3).equals("abbcbccabccacaabcaababbcabbcbcca"));
        //System.out.println(solution("cba", 1).equals("abcabc"));
    }


    public static String solution(String s, int k) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        while (k > 0) {
            k--;
            String ans = "";
            for (int i = 0; i < s.length(); i++) {
                char tem = s.charAt(i);
                if (tem == 'a') {
                    ans += "bc";
                } else if (tem == 'b') {
                    ans += "ca";
                } else if (tem == 'c') {
                    ans += "ab";
                }
            }
            s=ans;
        }
        return s;
    }
}
