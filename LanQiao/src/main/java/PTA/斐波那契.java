package PTA;

import java.util.Scanner;

public class 斐波那契 {
    public static void main(String[] args) {
        fei();
    }

    public static void fei(){
        Scanner scanner=new Scanner(System.in);
        int n = scanner.nextInt();
        if (n==0){
            System.out.println("0");
            return;
        } else if (n==1) {
            System.out.println("1");
            return;
        }
        int f1=0;
        int f2=1;
        while (true){
            int tem=f1+f2;
            if (tem>n){
                int lenr = tem - n;
                int lenl = n - f2;
                if (lenl<=lenr){
                    System.out.println(f2);
                    return;
                }else {
                    System.out.println(tem);
                    return;
                }
            }
            f1=f2;
            f2=tem;
        }
    }
}
