package 蓝桥杯真题.省12A组;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.省12A组
 * @Project：LanQiaoBei
 * @name：相乘
 * @Date：2025/4/10 20:51
 * @Filename：相乘
 */
public class 相乘 {
    public static void main(String[] args) {
        long bignum=1000000007;
        long chu=2021;
        long yu=999999999;
   /*     long x;
        for (int i = 1; i < bignum; i++) {
            long l = i * chu % bignum;
            if (l==yu){
                System.out.println(i);
                break;
            }
        }*/
        System.out.println("17812964");
        long num=17812964;
        for (long i = (num+1); i < bignum; i++) {
            long l = i * chu % bignum;
            if (l==yu){
                System.out.println(i);
                break;
            }
        }
        System.out.println("结束");
    }
}
