package luogu;

import java.util.Scanner;

public class 带分数 {
    //定义全局变量
    private static int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    //static int[] arr = {1,2,3,4};
    private static int cout = 0;//进行计数
    static int tar;//目标数字

    //进行分段，求解A，B，C三个数值
    static int cal(int s, int e) {
        int res = 0;
        for (int i = s; i < e; i++) {
            res = res * 10 + arr[i];
        }
        return res;
    }

    static void addCount() {
        for (int i = 1; i <= 7; i++) {
            int a = cal(0, i);//加号前面的数字
            //判断如果加号前面是数的数值大于所输出的数就直接退出
            if (a >= tar) {//+号前面的数进行判断是否大于或等于目标数
                continue;
            }
            //其次进行/除号判断，/号的选择与加号选择有如下关系
            //当加号在第1个位置时，/号必有7中位置选择,即需要进行7次判断
            //当加号在第2个位置时，/号必有6中位置选择，即需要进行6次判断
            for (int j = i + 1; j <= 8; j++) {

                int b = cal(i, j);//这是判断分子的数值
                int c = cal(j, 9);//这是判断分母数值，共9个数字。即下标最高是8
                if (a * c + b == tar * c) {//因为是/除法运算时会出现向下取整的情况，则需要转化为乘法操作
                    cout++;

                }

            }

        }
    }

    //全排列
    static void perm(int[] arr, int q, int p) {
        if (q == p) {//判断到最后一个元素时进行分段判断
            //先进行加号+取段，根据上面的算术特征，加号只有7中位置选择
            addCount();

        } else {
            for (int i = q; i < arr.length; i++) {
                //元素放置首位
                swap(i, q);
                perm(arr, q + 1, p);
                //回溯
                swap(i, q);
            }
        }
    }

    static void swap(int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        tar = sc.nextInt();
        perm(arr, 0, arr.length);
        System.out.println(cout);
    }
}


