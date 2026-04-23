package 蓝桥杯真题.决12A组;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.决12A组
 * @Project：LanQiaoBei
 * @name：纯质数2
 * @Date：2025/4/11 20:32
 * @Filename：纯质数2
 */
public class 纯质数2 {

    public static void main(String[] args) {
        help2();
    }
    static List<Integer> zhishu;
    static List<Integer> tans;
    public static void help2(){
        zhishu=new ArrayList<>();
        zhishu.add(2);
        zhishu.add(3);
        zhishu.add(5);
        zhishu.add(7);
        tans=new ArrayList<>();
        int bignum=20210605;
        for (int i = 2; i < bignum; i++) {
            if (isZhi(i)){
                int temp=i;
                int in=0;
                boolean flag=true;
                while (temp!=0){
                    in=temp%10;
                    temp=temp/10;
                    if (!zhishu.contains(in)){
                        flag=false;
                        break;
                    }
                }
                if (flag){
                    tans.add(i);
                    System.out.println(i);
                }
            }
        }
        System.out.println("tans.size:  "+tans.size());
    }

    public static boolean isZhi(int n){
        if (n==1){
            return false;
        }
        for (int i = 2; i *i<= n; i++) {
            if (n%i==0){
                return false;
            }
        }
        return true;
    }

}
