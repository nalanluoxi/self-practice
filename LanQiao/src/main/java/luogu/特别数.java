package luogu;

import java.util.ArrayList;
import java.util.Scanner;

public class 特别数 {


    public static void main(String[] args) {
        special();
    }

    public static void special(){
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt();
        ArrayList<Integer>arr=new ArrayList<>();
        arr.add(2);
        arr.add(0);
        arr.add(1);
        arr.add(9);
        int count=0;
        for (int i = 1; i <= n; i++) {
            int tem=i;
            while (tem!=0){
                int now = tem % 10;
                tem = tem / 10;
                if (arr.contains(now)){
                    count+=i;
                    break;
                }
            }
        }
        System.out.println(count);

    }


}
