package luogu;

import java.util.Scanner;

public class 数的计算 {
    public static void main(String[] args) {
        shu();
    }

    public static void shu() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        count(n);
        System.out.println(f[n]);
    }
    public static int[]f=new int[1001];


    public static void count(int n){
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <=(i/2) ; j++) {
                f[i]=f[i]+f[j];
            }
            f[i]++;
        }
    }

}
