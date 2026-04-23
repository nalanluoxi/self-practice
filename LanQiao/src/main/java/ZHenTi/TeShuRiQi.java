package ZHenTi;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class TeShuRiQi {
    public static void main(String[] args) {
        specialtime();
    }

    public static void specialtime(){
        LocalDate time1=LocalDate.of(1900,1,1);
        LocalDate time2=LocalDate.of(9999,12,31);
        //Date time2=new Date(5000,12,31);
        int  count=0;
        while (1==1){
            if (time1.equals(time2)){
                System.out.println(count);
                break;
            }
            //System.out.println(time1);
            int y1=time1.getYear()/1000;
            int y2=time1.getYear()/100-10*y1;
            int y3=time1.getYear()/10-y1*100-y2*10;
            int y4=time1.getYear()%10;


            int m1=time1.getMonthValue()/10;
            int m2=time1.getMonthValue()%10;

            int d1=time1.getDayOfMonth()/10;
            int d2=time1.getDayOfMonth()%10;

            if ((y1+y2+y3+y4)==(m1+m2+d1+d2)){
                count++;
            }
            System.out.println(time1+"     y1:"+y1+" y2:"+y2+"  y3:"+y3+"  y4:"+y4+"  m1"+m1+"  m2"+m2+"  d1"+d1+"  d2:"+d2);
            time1=time1.plusDays(1);
        }

    }
}
