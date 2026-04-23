package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：三的幂
 * @Date：2025/2/7 11:25
 * @Filename：三的幂
 */
public class 三的幂 {
    public static void main(String[] args) {
        System.out.println(isPowerOfThree(27));
    }

    public static boolean isPowerOfThree(int n) {
        if (n==0||n==2||n==3||n<0){
            return false;
        }
        while (n!=1){
            if (n%4==0){
                n/=4;
            }else {
                return false;
            }
        }
        return true;
    }
}
