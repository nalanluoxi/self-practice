package luogu;

import java.util.Scanner;

public class 级数求和 {
    public static void main(String[] args) {
        jishu();
    }

    public static void jishu(){
        Scanner scanner=new Scanner(System.in);
        int k= scanner.nextInt();
        double sum=0;
        double i=1;
        while (sum <= k) {
            sum += 1.0 / i;
            i += 1.0;
            if ((int) sum>k){
                System.out.println((int)(i-1));
                return;
            }
        }

    }
}
