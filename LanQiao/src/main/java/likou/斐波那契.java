package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：斐波那契
 * @Date：2025/1/26 10:40
 * @Filename：斐波那契
 */
public class 斐波那契 {
    public static void main(String[] args) {
        fib(5);
    }
    public static int fib(int n) {
        if (n<=0){
            return 0;
        }
        if (n<=2){
            return 1;
        }
        int f1=1;
        int f2=1;
        int f3=0;
        int index=3;
        while (index<=n){
            f3=f1+f2;
            f1=f2;
            f2=f3;
            index++;
        }
        //System.out.println(f3);
        return f3;
    }

}
