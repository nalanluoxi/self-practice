package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：最少前缀
 * @Date：2024/12/25 23:17
 * @Filename：最少前缀
 */
public class 最少前缀 {

    public static void main(String[] args) {
        System.out.println(solution("aba", "abb") == 1);
        System.out.println(solution("abcd", "efg") == 4);
        System.out.println(solution("xyz", "xy") == 1);
        System.out.println(solution("hello", "helloworld") == 0);
        System.out.println(solution("same", "same") == 0);
    }



    public static int solution(String S, String T) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        int count=0;
        if (S.length()>T.length()){
            count+=S.length()-T.length();
        }
        for (int i = 0; i < Math.min(S.length(),T.length()); i++) {
            char stemp = S.charAt(i);
            char ttemp = T.charAt(i);
            if (stemp!=ttemp){
                count++;
            }
        }
        return count;
    }



}
