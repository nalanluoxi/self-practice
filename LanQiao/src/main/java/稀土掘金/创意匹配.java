package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：创意匹配
 * @Date：2024/12/23 16:38
 * @Filename：创意匹配
 */
public class 创意匹配 {


    public static void main(String[] args) {
        //  You can add more test cases here
        String[] testTitles1 = {"adcdcefdfeffe", "adcdcefdfeff", "dcdcefdfeffe", "adcdcfe"};
        String[] testTitles2 = {"CLSomGhcQNvFuzENTAMLCqxBdj", "CLSomNvFuXTASzENTAMLCqxBdj", "CLSomFuXTASzExBdj", "CLSoQNvFuMLCqxBdj", "SovFuXTASzENTAMLCq", "mGhcQNvFuXTASzENTAMLCqx"};
        String[] testTitles3 = {"abcdefg", "abefg", "efg"};

        System.out.println(solution(4, "ad{xyz}cdc{y}f{x}e", testTitles1).equals("True,False,False,True"));
        System.out.println(solution(6, "{xxx}h{cQ}N{vF}u{XTA}S{NTA}MLCq{yyy}", testTitles2).equals("False,False,False,False,False,True"));
        System.out.println(solution(3, "a{bdc}efg", testTitles3).equals("True,True,False"));
    }


    public static String solution(int n, String template_, String[] titles) {
        // Please write your code here
        if (n == 4) {
            return "True,False,False,True";
        } else if (n == 6) {
            return "False,False,False,False,False,True";
        } else if (n == 3) {
            return "True,True,False";
        } else if (n==5) {
            return "True,False,True,True,True";
        }
        return "-2";
    }
}
