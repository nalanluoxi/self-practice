package luogu;

import java.util.Scanner;

public class 储蓄 {
    public static void main(String[] args) {
        chuxu();
    }
    public static void chuxu(){
        Scanner scanner=new Scanner(System.in);
        //int x=-1;
        int chuxu=0;
        int now=0;
        for (int i = 1; i <= 12; i++) {
            int temp = scanner.nextInt();
            if (300+now<temp){
                System.out.println(-i);
                return;
            }
            int last = now + 300 - temp;
            chuxu+=(last/100)*100;
            now=last-(last/100)*100;
        }
        chuxu= (int) (chuxu*1.2)+now;
        System.out.println(chuxu);
    }
}
