package luogu;

import java.text.DecimalFormat;
import java.util.Scanner;

public class 一元三次 {
    public static void main(String[] args) {
        sanci();
    }

    static double a;
    static double b;
    static double c;
    static double d;

    public static void sanci() {

        Scanner scanner = new Scanner(System.in);
        a = scanner.nextDouble();
        b = scanner.nextDouble();
        c = scanner.nextDouble();
        d = scanner.nextDouble();
        DecimalFormat df = new DecimalFormat( "0.00");
        double l,r,x1,x2,x3,m;
        int count = 0;
        for (int i = -100; i < 100; i++) {
            l=i;
            r=l+1;
            x1=getAns(l);
            x2=getAns(r);
            if (x1==0){
                System.out.print(df.format(l)+" ");
                count++;
            }

            if (x1*x2<0){
                while (r-l>=0.001){
                    m=(r+l)/2;
                    if (getAns(m)*getAns(r)<0){
                        l=m;
                    }else {
                        r=m;
                    }
                }
                System.out.print(df.format(r)+" ");
                count++;
            }
            if (count==3){
                return;
            }
        }

    }

    public static double getAns(double x) {
        return a * x * x * x + b * x * x + c * x + d;
    }


}
