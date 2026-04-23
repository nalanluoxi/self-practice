package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：组成字符串ku最大次数
 * @Date：2025/1/11 16:28
 * @Filename：组成字符串ku最大次数
 */
public class 组成字符串ku最大次数 {

    public static void main(String[] args) {
        System.out.println(solution("AUBTMKAxfuu") == 1);
        System.out.println(solution("KKuuUuUuKKKKkkkkKK") == 6);
        System.out.println(solution("abcdefgh") == 0);
    }

    public static int solution(String s) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        int  k=0;
        int  u=0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c=='k'||c=='K'){
                k++;
            }else if (c=='u'||c=='U'){
                u++;
            }
        }

        return Math.min(k,u);
    }

}
