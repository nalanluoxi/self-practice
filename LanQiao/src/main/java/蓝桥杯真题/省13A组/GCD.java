package 蓝桥杯真题.省13A组;

import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.省13A组
 * @Project：LanQiaoBei
 * @name：GCD
 * @Date：2025/4/2 17:03
 * @Filename：GCD
 */
public class GCD {

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        String[] split = scanner.nextLine().split(" ");
        int a=Integer.parseInt(split[0]);
        int b=Integer.parseInt(split[1]);
        int c=Math.abs(a-b);
        int d=c-a%c;
        System.out.println(d);
        /*int k=1;
        int g1 = gcd(a, b);
        while (k<=Math.min(a,b)){
            int g2 = gcd(a + k, b + k);
            if (g2>g1){
                System.out.println(k);
                return;
            }
        }*/
    }

    public static int gcd(int a,int b){
        if (b==0){
            return a;
        }
        return gcd(b,a%b);
    }
}
