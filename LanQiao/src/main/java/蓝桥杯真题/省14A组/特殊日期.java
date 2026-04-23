package 蓝桥杯真题.省14A组;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.省14A组
 * @Project：LanQiaoBei
 * @name：特殊日期
 * @Date：2025/3/26 11:10
 * @Filename：特殊日期
 */
public class 特殊日期 {
    public static void main(String[] args) {
        /*System.out.println(getAdd(1));
        System.out.println(getAdd(12));
        System.out.println(getAdd(1999));*/
        System.out.println("70910");
        //getDate();
    }

    public static void getDate(){
        LocalDate startDate = LocalDate.of(1900, 1, 1);
        LocalDate endDate = LocalDate.of(9999, 12, 31);
        List<LocalDate> dates = new ArrayList<>();
        long between = ChronoUnit.DAYS.between(startDate, endDate);
        for (int i = 0; i < between; i++) {
            LocalDate date = startDate.plusDays(i);
            dates.add(date);
        }
     /*   for (LocalDate date : dates) {
            System.out.println(date);
        }*/
        int count=0;
        for (LocalDate date : dates) {
            String[] split = date.toString().split("-");
            Integer year = Integer.valueOf(split[0]);
            Integer mon = Integer.valueOf(split[1]);
            Integer day = Integer.valueOf(split[2]);
            //System.out.println(year+" : "+mon+" : "+day);
            int addyear = getAdd(year);
            int addmonday = getAdd(mon) + getAdd(day);
            if (addyear == addmonday){
                count++;
                System.out.println("满足条件的日期: "+year+" : "+mon+" : "+day);
            }
        }
        System.out.println(count);
    }

    public static int getAdd(int n){
        int sum=0;
        while (n!=0){
            sum+=n%10;
            n=n/10;
        }
        return sum;
    }
}
