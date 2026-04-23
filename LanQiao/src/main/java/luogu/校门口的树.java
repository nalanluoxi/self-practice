package luogu;

import java.util.Scanner;

public class 校门口的树 {
    public static void main(String[] args) {
        shu();
    }
    public static void shu(){
        Scanner scanner=new Scanner(System.in);
        int l= scanner.nextInt();
        int n= scanner.nextInt();
        int[]arr=new int[1001];
        for (int i = 0; i < n; i++) {
            int start=scanner.nextInt();
            int end=scanner.nextInt();
            for (int j = start; j <= end; j++)
                arr[j]=1;
        }
        int count=0;
        for (int i = 0; i <= l; i++)
            if (arr[i]==0)count++;
        System.out.println(count);
    }
}
