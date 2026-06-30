package acm练习题;

import java.util.Arrays;
import java.util.Scanner;

public class 网易Test01 {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] split = scanner.nextLine().split(" ");
        int n = Integer.parseInt(split[0]);
        int x = Integer.parseInt(split[1]);
        int y = Integer.parseInt(split[2]);

        String[] arr = scanner.nextLine().split(" ");
        int[] brr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            brr[i] = Integer.parseInt(arr[i]);
        }
        test(n, x, y, brr);

    }


    // 怪物数 盾s 伤害 血条
    public static void test(int n, int x, int y, int[] arr) {
        Arrays.sort(arr);
        int ans = 0;
        int nowX=x;

        while (true) {
            Arrays.sort(arr);
            int minH = arr[0];

            int temp = minH % y == 0 ? 0 : 1;
            int t1 = minH / y +temp;
            if (t1<=nowX){
                ans+=t1;
                nowX-=t1;
                nowX+=x;
                arr[0]=minH*2;
            }else {
                System.out.println(ans+nowX);
                return;
            }

        }

    }


}
