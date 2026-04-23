package org.example;

import java.util.Scanner;

public class 瓷砖铺放 {
    public static void main(String[] args) {
    cizhuan();
    }
    public static int count;
    public static void cizhuan(){
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        count=0;
        help(n);
        System.out.println(count);
    }


    public static void help(int n){
        if (n==0){
            count++;
            return ;
        } else if (n<0) {
            return;
        }
        help(n-1);
        help(n-2);
    }

}
