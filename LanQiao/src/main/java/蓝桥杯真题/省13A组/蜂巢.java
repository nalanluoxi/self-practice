package 蓝桥杯真题.省13A组;

import java.util.Map;
import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.省13A组
 * @Project：LanQiaoBei
 * @name：蜂巢
 * @Date：2025/4/2 20:08
 * @Filename：蜂巢
 */
public class 蜂巢 {
    public static void main(String[] args) {
        fengchao();

    }

    static Scanner scanner = new Scanner(System.in);

    public static void fengchao() {
        String[] nums = scanner.nextLine().split(" ");
        int x1 = Integer.parseInt(nums[0]);
        int x2 = Integer.parseInt(nums[1]);
        int x3 = Integer.parseInt(nums[2]);

        int x4 = Integer.parseInt(nums[3]);
        int x5 = Integer.parseInt(nums[4]);
        int x6 = Integer.parseInt(nums[5]);

        int[] point1 = getPoint(x1, x2, x3);
        System.out.println(point1[0]+" : "+point1[1]);

        int[] point2 = getPoint(x4, x5, x6);
        System.out.println(point2[0]+" : "+point2[1]);
        int dx=Math.abs(point1[0]-point2[0]);
        int dy=Math.abs(point1[1]-point2[1]);

        if (dx>dy){
            System.out.println(0.5*(dx+dy));
            return;
        }else {
            System.out.println(dy);
            return;
        }
    }

    public static int[] getPoint(int x1, int x2, int x3) {
       int point[]=new int[2];
       point[0]=numx[x1]*x2 +numx[(x1+2)%6]*x3;
       point[1]=numy[x1]*x2 +numy[(x1+2)%6]*x3;
       return point;
    }

    static int[] numx = {-2, -1, 1, 2, 1, -1};
    static int[] numy = {0, 1, 1, 0, -1, -1};

}
