package luogu;

import java.util.Scanner;

public class 蚂蚁 {
    public static void main(String[] args) {
        mayi();
    }

    public static void mayi(){
        Scanner scanner=new Scanner(System.in);
        int x=0,y=0;

        int n= scanner.nextInt();
        int num1= scanner.nextInt();

        for (int i = 1; i < n; i++) {
            int temnum= scanner.nextInt();
            if (temnum>0&&Math.abs(temnum)<Math.abs(num1)){
                x++;
            } else if (temnum<0&&Math.abs(temnum)>Math.abs(num1)) {
                y++;
            }
        }

        if (num1<0){
            if (x==0){
                System.out.println(1);
            }else {
                System.out.println(x+y+1);
            }
        }else {
            if (y==0){
                System.out.println(1);
            }else {
                System.out.println(x+y+1);
            }
        }
    }


}
