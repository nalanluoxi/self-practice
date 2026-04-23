package luogu;

import java.util.Scanner;

public class 饮料换购 {
    public static void main(String[] args) {
        yinliap();
    }

    public static void yinliap(){
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt();
        int count=n;

        while (n>=3){
            int las = n % 3;
            int getnew = n / 3;
            count+=getnew;
            n=las+getnew;
        }
        System.out.println(count);
    }
}
