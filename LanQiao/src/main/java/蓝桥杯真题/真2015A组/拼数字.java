package 蓝桥杯真题.真2015A组;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.真2015A组
 * @Project：LanQiaoBei
 * @name：拼数字
 * @Date：2025/3/24 20:00
 * @Filename：拼数字
 */
public class 拼数字 {
/*    public static void main(String[] args) {
        System.out.println("5435123");
    }*/
public static void main(String[] args) {
    long a2 = 7385137888721L;
    long a1 = 10470245;
    long a = a2 + a1/4;
    long L = 1;
    while(a >= 2*L+1){
        a-= 2*L+1;
        L+=1;
    }
    System.out.println(2*L);
}
}
