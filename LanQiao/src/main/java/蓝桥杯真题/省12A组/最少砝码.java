package 蓝桥杯真题.省12A组;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.省12A组
 * @Project：LanQiaoBei
 * @name：最少砝码
 * @Date：2025/4/11 10:30
 * @Filename：最少砝码
 */
public class 最少砝码 {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        long l = scanner.nextLong();
         help(l);

    }

    public static void help(long n) {

       int now=1;
       int count=1;
       while (true){
           now=now*3+1;
           count++;
           if (now>=n){
               System.out.println(count);
               return;
           }
       }
    }

  /*  static Set<Long> set;
    static List<Long> list;

    static long left;
    static long right;

    public static long help(long n) {
       *//* if (n==29524){
            System.out.println(14762);
            return 14762;
        }*//*
        list = new ArrayList<>();
        list.add(1l);
        set = new TreeSet<>();
        set.add(1l);
        left = 1;
        right = n+1;
        for (long l = n - 1; l > 0; l--) {
            if (set.contains(l)) {
                continue;
            }
            int size = list.size();
            set.add(l);
            list.add(l);
            for (int i = 0; i < size; i++) {
                Long temp = list.get(i);
                long max = temp + l;
                long min = Math.abs(temp - l);
                *//*if (min==2){
                    System.out.println("min:"+min);
                }*//*
                if ( max <= n ) {
                    set.add(max);
                }
                if ( min >= 1){
                     set.add(min);
                }
            }
        }

        System.out.println(set+"  set size:"+set.size());
        System.out.println("list size:"+list.size());
        return list.size();
    }*/
}
