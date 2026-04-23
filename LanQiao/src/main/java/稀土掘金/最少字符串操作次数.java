package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：最少字符串操作次数
 * @Date：2024/12/27 16:37
 * @Filename：最少字符串操作次数
 */
public class 最少字符串操作次数 {



    public static void main(String[] args) {
        System.out.println(solution("abab") == 2);
        System.out.println(solution("aaaa") == 2);
        System.out.println(solution("abcabc") == 3);
    }


    public static int solution(String S) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        int []res=new int[26];
        for (int i = 0; i < S.length(); i++) {
            res[S.charAt(i)-'a']++;
        }
        int ans=0;
        for (int i = 0; i < res.length; i++) {
           if (res[i]>0){
               ans+=res[i]/2;
           }
        }

        return ans;
    }

}
