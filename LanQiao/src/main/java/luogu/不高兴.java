package luogu;

import java.util.Scanner;

public class 不高兴 {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        int []arr=new int[8];
        for (int i = 1; i <= 7; i++) {
            String[] s = scanner.nextLine().split(" ");
            int tem = Integer.valueOf(s[0]) + Integer.valueOf(s[1]);
            if (tem>8){
                arr[i]=tem;
            }
        }
        int max=9,index=0;

        for (int i = 7; i >=1 ; i--) {
            if (arr[i]>=max){
                max=arr[i];
                index=i;
            }
        }
        System.out.println(index);
    }
}
