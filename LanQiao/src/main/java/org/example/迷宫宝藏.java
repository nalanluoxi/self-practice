package org.example;

import java.util.Scanner;

public class 迷宫宝藏 {
    public static void main(String[] args) {
        migong();
    }

    public static int[] arr;

    public static void migong() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }

        while (true) {
            int lastZindex = getLast();
            if (lastZindex != -1) {
                int gount = getGount(lastZindex);
                System.out.println(gount);
                return;
            } else {
                int getfirst = getfirst();
                remove(arr[getfirst]);
            }
        }
    }

    public static void remove(int num) {
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] == num) {
                arr[i] = 0;
            }
        }
    }

    public static int getfirst() {
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] != 0) {
                return i;
            }
        }
        return -1;
    }

    public static int getLast() {
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    public static int getGount(int lastindex) {
        int[] haxi = new int[10];
        for (int i = 0; i < lastindex; i++) {
            int t = arr[i];
            haxi[t]++;
        }
        int count = 0;
        for (int i = 0; i < haxi.length; i++) {
            if (haxi[i] != 0) {
                count++;
            }
        }
        return count;
    }


}
