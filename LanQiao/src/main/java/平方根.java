/**
 * @Author 纳兰洛熙
 * @Package：PACKAGE_NAME
 * @Project：LanQiaoBei
 * @name：平方根
 * @Date：2025/4/25 16:44
 * @Filename：平方根
 */
public class 平方根 {
    public static void main(String[] args) {
        //System.out.println(mySqrt(8));
        System.out.println(mySqrt(2147395599));
    }

    public static int mySqrt(int x) {
        long left = 1;
        long right = x;

        while (left<=right){
            long mid = left+(right-left)/2;
            long all = mid * mid;
            if (all == x){
                return (int) mid;
            }else if (all>x){
                right = mid-1;
            } else if (all < x) {
                left = mid+1;
            }
        }
        return (int) right;
    }
}
