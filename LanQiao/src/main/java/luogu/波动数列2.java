package luogu;

import java.util.Scanner;

public class 波动数列2 {
    public static void main(String[] args) {
        shuzu();
    }
    public static int n;
    public static int s;
    public static int a;
    public static int b;
    public static int count;

    public static void shuzu(){
        Scanner scanner=new Scanner(System.in);
        n= scanner.nextInt();
        s= scanner.nextInt();
        a= scanner.nextInt();
        b= scanner.nextInt();

        for (int i = -9; i <= 9; i++) {
            help(i,0,i);
        }
        System.out.println(count%100000007);
    }


    public static int sum=0;
    public static void help(int first,int lens,int now){
        if (first>9){
            return;
        }
        if (lens==n){
            if (s==sum){
                count++;
            }
            sum=0;
            return;
        }
        sum+=now;
        help(first,lens+1,now+a);
        help(first,lens+1,now-b);

    }

}
