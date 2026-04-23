package 蓝桥杯真题.省14A组;

import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.省14A组
 * @Project：LanQiaoBei
 * @name：棋盘
 * @Date：2025/3/27 21:16
 * @Filename：棋盘
 */
public class 棋盘 {

    static int [][]nums;

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt();
        int t=scanner.nextInt();
        scanner.nextLine();
        nums=new int[n][n];
        for (int i = 0; i < t; i++) {
            String[] split = scanner.nextLine().split(" ");
            int x1=Integer.parseInt(split[0]);
            int y1=Integer.parseInt(split[1]);
            int x2=Integer.parseInt(split[2]);
            int y2=Integer.parseInt(split[3]);
            fanZhuan(x1,y1,x2,y2);
        }
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums[0].length; j++) {
                System.out.print(nums[i][j]);
            }
            System.out.println();
        }
    }


    public static void fanZhuan(int x1,int y1,int x2,int y2){
        for (int i = x1-1; i <= x2-1; i++) {
            for (int j = y1-1; j <= y2-1; j++) {
                if (nums[i][j]==0){
                    nums[i][j]=1;
                } else if (nums[i][j]==1) {
                    nums[i][j]=0;
                }
            }
        }
    }
}
