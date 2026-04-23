package luogu;

import java.util.Scanner;

public class 幂次方 {
    public static void main(String[] args) {
        mici();
    }

    public static void mici(){
        Scanner scanner=new Scanner(System.in);
        int num= scanner.nextInt();

    }

    public static void pow(int num){
        if (num>3){
            int s=0;
            int b=2;
            while (b<=num){
                b*=2;
                s++;
            }
            num=num-b/2;
            System.out.println("2(");
            pow(s);
            if (s == 3) {
                System.out.println("2+2(0)");
            }
            if (s==2){
                System.out.println("2");
            }
            if (s==1){
                System.out.println("2(0)");
            }
            System.out.println(")");

            if (num==1){
                System.out.println("+2(0)");
            }
            if (num==2){
                System.out.println("+2");
            }
            if (num==3){
                System.out.println("+2+2(0)");
            }
            if (num>3){
                System.out.println("+");
                pow(num);
            }
        }

    }

}