package 蓝桥杯真题.国2015A组;

import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.国2015A组
 * @Project：LanQiaoBei
 * @name：整数变换
 * @Date：2025/3/25 10:51
 * @Filename：整数变换
 */
public class 整数变换 {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        int num = scanner.nextInt();
        howMany(num,0);
    }

    public static void howMany(int n,int time){
        if (n==0){
            System.out.println(time);
            return;
        }
        int add = getAllAdd(n);
        howMany(n-add,time+1);
    }

    public static int getAllAdd(int n){
        if (n==0){
            return 0;
        }
        return getAllAdd(n/10)+(n%10);
    }


}
