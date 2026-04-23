package luogu;

import java.util.Scanner;

public class A_B数对2 {
    public static void main(String[] args) {
        AB();
    }
    static Scanner scanner=new Scanner(System.in);

    public static void AB(){
        int N= scanner.nextInt();
       // long c = scanner.nextLong();
        int c = scanner.nextInt();
        int [] arr=new int[200001];
        int []brr=new int[N];
        for (int i = 0; i < N; i++) {
            int num = scanner.nextInt();
            arr[num]++;
            brr[i]=num;
        }
        Long count= 0L;
        for (int i = 0; i < brr.length; i++) {
            int b = brr[i];
            int a=b+c;
            if (a!=b){
                count+=arr[a];
            }else {
                count+=arr[a]-1;
            }
        }
        System.out.println(count);

    }
}
