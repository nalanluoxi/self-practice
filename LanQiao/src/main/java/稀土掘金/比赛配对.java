package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：比赛配对
 * @Date：2024/12/27 16:14
 * @Filename：比赛配对
 */
public class 比赛配对 {



    public static void main(String[] args) {
        System.out.println(solution(7) == 6);
        System.out.println(solution(14) == 13);
        System.out.println(solution(1) == 0);


        //System.out.println(7/2);
    }



    public static int solution(int n) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        int ans=0;
        while (true){
            ans+=n/2;
            n=(n%2+n/2);
            if (n==1){
                break;
            }
        }
        return ans;
    }





}
