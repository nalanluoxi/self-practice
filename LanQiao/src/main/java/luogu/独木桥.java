package luogu;

import java.util.Scanner;

public class 独木桥 {
    public static void main(String[] args) {
        dumuqiao();
    }

    public static void dumuqiao() {
        Scanner scanner = new Scanner(System.in);
        int L = scanner.nextInt();
        int N = scanner.nextInt();
        int[][] arr = new int[N][2];
        for (int i = 0; i < N; i++) {
            int tem = scanner.nextInt();
            int l = tem;
            int r = L + 1 - l;
            arr[i][0] = Math.min(l,r);
            arr[i][1] = Math.max(l,r);
        }
        int min = 0, max = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i][0] > min) {
                min = arr[i][0];
            }
            if (arr[i][1] > max) {
                max = arr[i][1];
            }
        }
        System.out.println(min  + " " + max);

    }
}
