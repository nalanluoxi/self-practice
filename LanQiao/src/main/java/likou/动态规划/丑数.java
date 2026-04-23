package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：丑数
 * @Date：2025/4/8 18:45
 * @Filename：丑数
 */
public class 丑数 {
    public static void main(String[] args) {
        System.out.println(isUgly(14));
    }

    public static boolean isUgly(int n) {
        if (n<=0){
            return false;
        }
        int [] list=new int[]{2,3,5};
        for (int i : list) {
            while (n%i==0){
                n/=i;
            }
        }
        return n==1;
    }

}
