package 蓝桥杯真题.省13A组;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.省13A组
 * @Project：LanQiaoBei
 * @name：求和
 * @Date：2025/4/2 16:52
 * @Filename：求和
 */
public class 求和 {
    public static void main(String[] args) {
        long l = 20230408L;
        /*dp(l);*/
        System.out.println(l*(l+1)/2);
        System.out.println("204634714038436");
    }

    static long tempnum=0;
    public static long dp(long n){
        if (n==0){
            System.out.println(tempnum);
            return 0;
        }
        tempnum = tempnum + n;
        return dp(n-1);
    }
}
