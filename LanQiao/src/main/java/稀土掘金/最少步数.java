package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：最少步数
 * @Date：2025/1/17 8:51
 * @Filename：最少步数
 */
public class 最少步数 {

    public static void main(String[] args) {
        // You can add more test cases here
       // System.out.println(solution(12, 6) == 4);
        System.out.println(solution(34, 45) == 6);
       // System.out.println(solution(50, 30) == 8);
    }

    public static int solution(int x_position, int y_position) {
        // Please write your code here
        int length=Math.abs(y_position-x_position);
        System.out.println(length);
        if (length%2==0){
            return getCount(length/2)*2;
        }else {
            return getCount(length/2)*2+1;
        }
      //34  +1/35/1  +2/37/3  +3/40/6 +3/43/9  +2/45/9 +1/44/10
    }

    public static int getCount(int temp){
        int sum=0;
        int count=0;
        int steps=1;
        while (sum<=temp) {
            sum += steps;
            steps++;
            count++;
        }
        System.out.println(count);
        return count;
    }


}
