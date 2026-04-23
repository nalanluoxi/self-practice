package 蓝桥杯真题.真2015A组;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.真2015A组
 * @Project：LanQiaoBei
 * @name：召唤数学精灵
 * @Date：2025/3/24 20:01
 * @Filename：召唤数学精灵
 */
public class 召唤数学精灵 {
    public static void main(String[] args) {

        Long a = 2024041331404202L;
        Long a1=105136599l;
        //long a=200l;
        long ans=0;
        for (Long l = 0l; l <= 10l; l++) {
            if (isTrue(l)){
                System.out.println("符合条件的数字 ： "+l);
                ans++;
            }
        }
        System.out.println("ans :"+ ans);

        System.out.println(a/200l*4);
        System.out.println("10120206657021");
    }

    public static boolean isTrue(Long a) {
        long add = add(a);
        long cheng = cheng(a);
        long cha = add - cheng;
        if (cha<100){
            return false;
        }
        if (cha % 100 == 0) {
            return true;
        } else {
            return false;
        }
    }

    /*static List<Long> andList;
    static List<Long>  chengList;*/

    static Long addFirst=1l;

    static Long chengFirst=1l;

    public static long add(long a) {
        if (a==1){
            return 1;
        }
        addFirst=addFirst +a;
        return addFirst;
    }

    public static long cheng(long a) {
        if (a==1){
            return 1;
        }
        chengFirst=chengFirst *a;
        return chengFirst;
    }

}
