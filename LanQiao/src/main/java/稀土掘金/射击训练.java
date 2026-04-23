package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：射击训练
 * @Date：2025/1/19 16:14
 * @Filename：射击训练
 */
public class 射击训练 {



    public static void main(String[] args) {
        System.out.println(solution(1, 0) == 10);
        System.out.println(solution(1, 1) == 9);
        System.out.println(solution(0, 5) == 6);
        System.out.println(solution(3, 4) == 6);
    }


    public static int solution(int x, int y) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        double r = Math.pow((x * x + y * y), 0.5);
        if (r==0){
            return 0;
        }
        return(int)(11.0- r);
    }

}
