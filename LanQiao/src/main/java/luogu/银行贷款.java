package luogu;

import java.text.DecimalFormat;
import java.util.Scanner;

public class 银行贷款 {


    public static void main(String[] args) {
        daikuan();
    }

    static Scanner scanner = new Scanner(System.in);
    static DecimalFormat df = new DecimalFormat("0.0");

   static long ben;
   static long huan;
   static int yue;

    public static void daikuan() {
        ben = scanner.nextLong();
        huan = scanner.nextLong();
        yue = scanner.nextInt();

        solve();
    }

    public static void solve() {
        double l=0,r=5;
        while (l+0.0001<r){
            double m = (l + r) / 2;
            if (check(m)){
                l=m;
            }else {
                r=m;
            }
        }
        System.out.printf("%.1f",l*100);
    }

    public static boolean check(double k){
        double money=ben;
        for (int i = 1; i <= yue; i++) {
            money=money*(1+k)-huan;
        }
        if (money>0){
            return false;
        }
        return true;
    }

}

