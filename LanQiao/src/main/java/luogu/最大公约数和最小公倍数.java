package luogu;

import java.util.Scanner;

public class 最大公约数和最小公倍数 {
    public static void main(String[] args) {
        pq();
    }

    public static void pq() {
        Scanner scanner = new Scanner(System.in);
        int x = scanner.nextInt();
        int y = scanner.nextInt();

        if (x==y){
            System.out.println(1);
            return;
        }
        int P = x;
        int Q = y;
        int count = 0;
        int tem=2;
        while (true) {
            if (isYue(P, Q, x) && isBei(P, Q, y)) {
                count++;
            }
            P=x*tem;
            Q=y/tem;
            tem++;
            if (P>Q){
                break;
            }
        }
        count=count*2;
        System.out.println(count);
    }

    public static boolean isYue(int a, int b, int y) {
        if (yue(a, b) == y)
            return true;
        return false;
    }

    public static int yue(int a, int b) {
        int tem = -1;
        while (true) {
            tem = a % b;
            if (tem == 0) {
                break;
            }
            a = b;
            b = tem;
        }
        return b;
    }

    public static boolean isBei(int a, int b, int B) {
        int i = a * b / yue(a, b);
        if (i == B) {
            return true;
        }
        return false;
    }
}
