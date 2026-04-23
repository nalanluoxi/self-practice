package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：二的幂
 * @Date：2025/2/7 10:33
 * @Filename：二的幂
 */
public class 二的幂 {
    public static void main(String[] args) {
        System.out.println(isPowerOfTwo(-16));
    }

    public static boolean isPowerOfTwo(int n) {
        if (n==0){
            return false;
        }
    /*    if (n<0){
            n=-n;
        }*/
        while (n!=1){
            if (n%2==0){
                n/=2;
            }else {
                return false;
            }
        }
        return true;
    }
}
