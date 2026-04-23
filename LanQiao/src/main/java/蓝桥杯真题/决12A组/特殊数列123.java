package 蓝桥杯真题.决12A组;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.决12A组
 * @Project：LanQiaoBei
 * @name：特殊数列123
 * @Date：2025/4/11 21:21
 * @Filename：特殊数列123
 */
public class 特殊数列123 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int maxn = 0;
        int[] arr = new int[n];
        int[] brr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
            brr[i] = scanner.nextInt();
            maxn = Math.max(maxn, Math.max(arr[i], brr[i]));
        }
        max = maxn;
        help(arr, brr);
    }

    public static void help(int[] arr, int[] brr) {
        init();
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            int start = arr[i];
            int end = brr[i];
            if (start == end) {
                System.out.println(list.get(start) - list.get(start - 1));
            } else {
                System.out.println(list.get(end) - list.get(start - 1));
            }
        }
    }

    static List<Long> list;
    static long max;

    public static void init() {
        list = new ArrayList<>();
        list.add(0l);
        list.add(1l);
        int lastmax = 2;
        int add = 1;
        for (long l = 1; l < max; l++) {
            Long befor = list.get(list.size() - 1);
            if (add == lastmax + 1) {
                lastmax++;
                add = 1;
            }
            list.add(befor + add);
            add++;
        }
        //System.out.println("init end ");
    }
}
