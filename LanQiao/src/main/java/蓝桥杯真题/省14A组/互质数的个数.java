package 蓝桥杯真题.省14A组;

import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.省14A组
 * @Project：LanQiaoBei
 * @name：互质数的个数
 * @Date：2025/3/27 20:39
 * @Filename：互质数的个数
 */
public class 互质数的个数 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long a = scanner.nextLong();
        long b = scanner.nextLong();
        huzhishu(a, b);

    }

    public static void huzhishu(long a, long b) {
        long ans = 1;
        double pow = Math.pow((double) a, (double) b);
        long big = (long) pow;
        if (isZhishu(big)){
            //技术所有质数
            System.out.println((big-2)%mod);
            return;
        }else {
            for (long i = 2; i < big; i++) {
                if ((isZhishu(i) && big%i!=0)||isHuZhi(i, big)) {
                    ans++;
                    ans=ans%mod;
                    //System.out.println("满足条件的数字： "+ i+"  当前ans: "+ans);
                }
            }
        }
        System.out.println(ans);
    }

    public static boolean isZhishu(long num) {
        if (num == 0 || num == 1) {
            return false;
        }
        for (long i = 2; i * i <= num; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isHuZhi(long a, long big) {
        if (a == 1 || big == 1 || a == big) {
            return true;
        }
        return bigYue(a, big) == 1;
    }

    public static long bigYue(long a, long b) {
        if (a == 0) {
            return b;
        }
        return bigYue(b % a, a);
    }
    static long mod = 998244353;

    /*public static void huzhishu(long a, long b) {
        long ans = 1;
        double pow = Math.pow((double) a, (double) b);
        long big = (long) pow;
        for (long i = 2; i < big; i++) {
            if (isHuZhi(i, big)) {
                ans++;
                ans = ans % mod;
                //  System.out.println("满足条件： "+i +"  现在ans: "+ans);
                //ans%=mod;
            }
        }
        System.out.println(ans);
    }

    public static boolean isHuZhi(long a, long big) {
        if (a == 1 || big == 1 || a == big) {
            return true;
        }
        return bigYue(a, big) == 1;
    }

    public static long bigYue(long a, long b) {
        if (a == 0) {
            return b;
        }
        return bigYue(b % a, a);
    }
*/
}
