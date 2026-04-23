package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：数字翻译器
 * @Date：2025/1/26 10:15
 * @Filename：数字翻译器
 */
public class 数字翻译器 {


    public static void main(String[] args) {
        // You can add more test cases here
        System.out.println(solution(12258) == 5);
        System.out.println(solution(1400112) == 6);
        System.out.println(solution(2110101) == 10);
    }

    public static int solution(int num) {
        // Please write your code here
        String strnum = String.valueOf(num);
        int len = strnum.length();
        int count=0;
        int[]dp=new int[strnum.length()+1];
        dp[0]=1;
        dp[1]=1;
        for (int i = 2; i < strnum.length()+1; i++) {
            char c1 = strnum.charAt(1);
            char c2 = strnum.charAt(i - 1);
            Integer n = Integer.valueOf(c2 + c1);
            if (n!=0){
                dp[i]=dp[i-1];
            }
            if (n>=10&&n<=25){
                dp[i]+=dp[i-2];
            }
        }
        return dp[len];
    }
}
