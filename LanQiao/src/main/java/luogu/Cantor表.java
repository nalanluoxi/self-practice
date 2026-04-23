package luogu;

import java.util.Scanner;

public class Cantor表 {
    public static void main(String[] args) {
        cantor();
    }

    public static void cantor(){
        Scanner scanner=new Scanner(System.in);
        long n= scanner.nextLong();
        long ceng=1;
        while (true){
            if ((n-ceng)<=0){
                if (n==(ceng-n+1)){
                    System.out.println((ceng-n+1)%n);
                    return;
                }
                System.out.println((ceng-n+1)+"/"+n);
                return;
            }
            n-=ceng;
            ceng++;
        }

    }
}
