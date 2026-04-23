package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：代币回本
 * @Date：2024/12/24 19:43
 * @Filename：代币回本
 */
public class 代币回本 {


    public static void main(String[] args) {
        System.out.println(solution(10, 1) == 10);
        System.out.println(solution(10, 2) == 5);
        System.out.println(solution(10, 3) == 4);
    }


    public static int solution(int a, int b) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        int time=0;
        while (a>0){
            a-=b;
            time++;
        }
        return time;
    }
}
