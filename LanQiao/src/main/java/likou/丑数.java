package likou;

/**
 * @Author 纳兰洛熙
 * @Package：luogu
 * @Project：LanQiaoBei
 * @name：丑数
 * @Date：2025/3/18 21:21
 * @Filename：丑数
 */
public class 丑数 {
    public static void main(String[] args) {
        int i = nthUglyNumber(3, 2, 3, 5);
        System.out.println(i);
    }

/*
    public static int nthUglyNumber(int n, int a, int b, int c) {
        int ans = 0;


        return ans;
    }
*/


    public static int nthUglyNumber(int n, int a, int b, int c) {
        long l = 0;
        long x = lcm(a, b);//6
        long y = lcm(a, c);//10
        long z = lcm(b, c);//15
        long k = lcm((int) x, c);//30
        long r = (long) (Math.min(Math.min(a, b), c)) * n;
        while (l + 1 < r) {
            long mid = l + (r - l) / 2;
            if (check(n, a, b, c, x, y, z, mid, k)) {
                r = mid;
            } else {
                l = mid;
            }
        }
        return (int) r;
    }

    private static boolean check(int n, int a, int b, int c, long x, long y, long z, long mid, long k) {
        long ans = mid / a + mid / b + mid / c - mid / x - mid / y - mid / z + mid / k;
        return ans >= n;
    }


    //最大公约数
    private static int gcb(int a, int b) {
        return b == 0 ? a : gcb(b, a % b);
    }

    //最小公倍数
    private static long lcm(int a, int b) {
        return (long) a * b / gcb(a, b);
    }


}
