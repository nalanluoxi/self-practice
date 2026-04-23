package luogu;

import java.util.Scanner;

public class 带分数2 {

    public static int[] num = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    public static int count = 0;
    public static int tar;

    public static void main(String[] args) {
        daifenshu();
    }

    public static int getnum(int startindex, int endindex) {
        int res = 0;
        for (int i = startindex; i < endindex; i++) {
            res = res * 10 + num[i];
        }
        return res;
    }

    public static void addCount() {
        for (int i = 1; i <= 7; i++) {
            int a = getnum(0, i);
            if (a >= tar)
                continue;
            for (int j = i + 1; j <= 8; j++) {
                int b = getnum(i, j);
                int c = getnum(j, 9);
                if (a * c + b == tar * c)
                    count++;
            }
        }
    }

    public static void bian(int[] num,int start, int end) {
        if (start == end) {
            addCount();
        } else {
            for (int i = start; i < end; i++) {
                swap(i,start);
                bian(num,start + 1, end);
                swap(i,start);
            }
        }
    }

    public static void swap(int i, int j) {
        int tem = num[i];
        num[i] = num[j];
        num[j] = tem;
    }

    public static void daifenshu() {
        Scanner scanner = new Scanner(System.in);
        tar = scanner.nextInt();
        bian(num,0,num.length);
        System.out.println(count);
    }


}
