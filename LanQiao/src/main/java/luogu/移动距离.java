package luogu;

import java.util.Scanner;

public class 移动距离 {
    public static void main(String[] args) {

        juli();
    }

    public static void juli() {
        Scanner scanner = new Scanner(System.in);
        int w = scanner.nextInt();
        int m = scanner.nextInt();
        int n = scanner.nextInt();

        if (m > n) {
            int tem = m;
            m = n;
            n = tem;
        }
        int[] mgetxy = getxy(m, w);
        //pirntall(mgetxy);
        int[] ngetxy = getxy(n, w);
        //pirntall(ngetxy);
        System.out.println(ngetxy[0] - mgetxy[0] + ngetxy[1] - mgetxy[1]);

    }

    public static void pirntall(int[] num) {
        System.out.println("x: " + num[0] + " y: " + num[1]);
    }

    public static int[] getxy(int target, int w) {
        int[] num = new int[2];
        if (target <= w) {
            num[0] = target;
            num[1] = 1;
            return num;
        }
        int y = target / w;
        int x = target % w;
        if (x==0){
            num[1]=y;
        }else {
            num[1]=y+1;
        }
        //行基 正序
        if (num[1]%2!=0){
            if (x==0){
                num[0]=w;
            }else {
                num[0] = x;
            }
        }
        //行偶数 倒序
        else {
            if (x==0){
                num[0]=1;
            }else {
                num[0]=w-x+1;
            }
        }
        return num;
    }
}
