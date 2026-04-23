package example.java2;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2
 * @Project：LanQiaoBei
 * @name：转化二进制
 * @Date：2025/3/23 11:19
 * @Filename：转化二进制
 */
public class 转化二进制 {
    public static void main(String[] args) {
        er(16,2);
        er(15,2);
        er(32,2);
        er(31,2);
    }

    public static void er(int n,int i){
        String s = "";
        while (n!=0){
            int t = n % i;
            s=t+s;
            n=n/i;
        }
        System.out.println(s);
    }
}
