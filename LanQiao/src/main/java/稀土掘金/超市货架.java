package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：超市货架
 * @Date：2024/12/24 19:33
 * @Filename：超市货架
 */
public class 超市货架 {



    public static void main(String[] args) {
        System.out.println(solution(3, 4, "abc", "abcd") == 3);
        System.out.println(solution(4, 2, "abbc", "bb") == 2);
        System.out.println(solution(5, 4, "bcdea", "abcd") == 4);
    }


    public static int solution(int n, int m, String s, String c) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        int[] shop=new int[26];
        int[] target=new int[26];

        for (int i = 0; i < s.length(); i++) {
            char temc = s.charAt(i);
            shop[temc-97]++;
        }

        for (int i = 0; i < c.length(); i++) {
            char temc = c.charAt(i);
            target[temc-97]++;
        }

        int count=0;
        for (int i = 0; i < target.length; i++) {
            if (target[i]!=0){
                int tarnum = target[i];
                int shopnum = shop[i];
                if (tarnum>shopnum){
                    count+=shopnum;
                } else if (tarnum <= shopnum) {
                    count+=tarnum;
                }
            }
        }

        return count;
    }


}
