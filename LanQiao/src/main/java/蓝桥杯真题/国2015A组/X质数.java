package 蓝桥杯真题.国2015A组;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题
 * @Project：LanQiaoBei
 * @name：国2015A组
 * @Date：2025/3/25 9:57
 * @Filename：国2015A组
 */
public class X质数 {
    public static void main(String[] args) {

    }

    public static void xZhi(long n) {

    }

    public static boolean isZhiShu(long n) {
        if (n == 0 || n == 1) {
            return false;
        }
        for (long i = 2l; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

}
