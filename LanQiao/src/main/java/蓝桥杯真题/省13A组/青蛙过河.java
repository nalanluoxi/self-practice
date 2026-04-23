package 蓝桥杯真题.省13A组;

import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.省13A组
 * @Project：LanQiaoBei
 * @name：青蛙过河
 * @Date：2025/4/2 18:01
 * @Filename：青蛙过河
 */
public class 青蛙过河 {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        String[] num1 = scanner.nextLine().split(" ");
        int n = Integer.parseInt(num1[0]);
        int d = Integer.parseInt(num1[1]);
        int[] nums=new int[n];
        for (int i = 0; i < n; i++) {
            nums[i]=scanner.nextInt();
        }
    }

}
