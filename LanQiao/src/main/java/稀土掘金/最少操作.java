package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：最少操作
 * @Date：2025/1/3 20:14
 * @Filename：最少操作
 */
public class 最少操作 {


    public static void main(String[] args) {
        //System.out.println(solution(2, 3) == 2 ? 1 : 0);
       // System.out.println(solution(4, 7) == 2 ? 1 : 0);
        System.out.println(solution(3, 66) == 9 ? 1 : 0);
    }



    public static int solution(int x, int y) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        int count=0;
        while (x!=y){
            if (y<x){
                count+=(x-y);
                break;
            }
            if (y%2==0){
                y/=2;
            }else {
                y+=1;
            }
            count++;
        }
        return count; // Placeholder return
    }
}
