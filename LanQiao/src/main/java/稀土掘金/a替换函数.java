package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：a替换函数
 * @Date：2025/1/23 16:42
 * @Filename：a替换函数
 */
public class a替换函数 {


    public static void main(String[] args) {
        System.out.println(solution("abcdwa").equals("%100bcdw%100"));
        System.out.println(solution("banana").equals("b%100n%100n%100"));
        System.out.println(solution("apple").equals("%100pple"));
    }

    public static String solution(String s) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        String res="";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c=='a'){
                res+="%100";
            }else {
                res+=c;
            }
        }
        return res; // Placeholder
    }
}
