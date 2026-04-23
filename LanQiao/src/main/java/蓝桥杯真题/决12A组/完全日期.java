package 蓝桥杯真题.决12A组;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.决12A组
 * @Project：LanQiaoBei
 * @name：完全日期
 * @Date：2025/4/11 20:23
 * @Filename：完全日期
 */
public class 完全日期 {
    public static void main(String[] args) {
        LocalDate start = LocalDate.of(2001, 1, 1);
        LocalDate end = LocalDate.of(2021, 12, 31);
        init();
        long count=0;
        while (start.isBefore(end)){
            String str = start.toString().replace("-", "");
            start=start.plusDays(1);
            if (isTrue(str)){
                count++;
                System.out.println(str);
            }
        }
        String str = start.toString().replace("-", "");
        if (isTrue(str)){
            count++;
        }
        System.out.println("count  : "+count);
        System.out.println("3356");
    }

    public static boolean isTrue(String str) {
        long sum=0;
        for (int i = 0; i < str.length(); i++) {
            long now = str.charAt(i) - '0';
            sum+=now;
        }
        return isWan(sum);
    }

    static List<Double> wan;
    public static void init(){
        wan=new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            wan.add(Math.pow(i,2));
        }
    }

    public static boolean isWan(long num){
        Double n=num*1.0;
        return wan.contains(n);
    }


}
