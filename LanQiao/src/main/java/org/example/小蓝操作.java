package org.example;

import java.util.Scanner;

public class 小蓝操作 {
    public static void main(String[] args) {
        mofa();
    }

    public static int[]arr;
    public static int[]brr;
    public static int ph;
    public static void mofa(){
        Scanner scanner=new Scanner(System.in);
        int n= scanner.nextInt();
        arr=new int[n+1];
        brr=new int[n+1];
        for (int i = 1; i < arr.length; i++) {
            arr[i]= scanner.nextInt();
        }
        for (int i = 1; i < brr.length; i++) {
            brr[i]= scanner.nextInt();
        }
        ph=0;
        for (int i = 1; i < n; i++) {
            if (brr[i]==arr[i]){
                continue;
            }
            int k=brr[i]-arr[i];
            arr[i]+=k;
            arr[i+1]-=k;
            ph+=Math.abs(k);
        }
        System.out.println(ph);
    }
}
