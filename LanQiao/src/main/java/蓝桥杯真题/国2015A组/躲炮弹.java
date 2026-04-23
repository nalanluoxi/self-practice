package 蓝桥杯真题.国2015A组;

import likou.删除倒数第n个;

import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.国2015A组
 * @Project：LanQiaoBei
 * @name：躲炮弹
 * @Date：2025/3/25 11:50
 * @Filename：躲炮弹
 */
public class 躲炮弹 {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        int n = scanner.nextInt();
        int l = scanner.nextInt();
        int r = scanner.nextInt();
        paodan(n,l,r);
    }

    public static void paodan(int n,int l ,int r){
        int l1 = n - l + 1>0?n - l + 1:Integer.MAX_VALUE;
        r++;
        while (!isZhi(r)){
            r++;
        }
        int l2 = r - n;
        int min = Math.min(l1, l2);
        System.out.println(min);
    }

    public static boolean isZhi(int n){
        if (n==0||n==1){
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n%i==0){
                return false;
            }
        }
        return true;
    }
}
