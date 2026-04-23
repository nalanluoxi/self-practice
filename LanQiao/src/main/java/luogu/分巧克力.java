package luogu;

import java.util.ArrayList;
import java.util.Scanner;

public class 分巧克力 {
    public static void main(String[] args) {
        qiaokeli();
    }

    public static int N;
    public static int K;
    public static ArrayList<Integer> arr;

    public static void qiaokeli() {
        Scanner scanner = new Scanner(System.in);
        N = scanner.nextInt();
        K = scanner.nextInt();
        int[][] arr = new int[N][2];
        for (int i = 0; i < N; i++) {
            arr[i][0] = scanner.nextInt();
            arr[i][1] = scanner.nextInt();
        }
        int l = 0, r = 20 ,mid=0;
        while (l < r) {
             mid = l + (r - l) / 2;
            if (true == helpfun(mid, arr, K)) {
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        System.out.println(mid);
    }

    public static boolean helpfun(int len, int[][] arr, int k) {
        int num = 0;
        for (int i = 0; i < arr.length; i++) {
            num = num + (arr[i][0] / len) * (arr[i][1] / len);
            if (num > k) {
                return true;
            }
        }

        return false;
    }


}
