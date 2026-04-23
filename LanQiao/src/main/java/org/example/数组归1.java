package org.example;

import java.util.Scanner;

public class 数组归1 {
    public static void main(String[] args) {
    gui();
    }

    public static int[]arr;
    public static int count;
    public static void gui(){
        Scanner scanner=new Scanner(System.in);
        int n= scanner.nextInt();
        arr=new int[n];
        for (int i = 0; i < arr.length; i++) {
            arr[i]= scanner.nextInt();
        }
        count=0;
        int i=0;
        while (i<arr.length){
            if (arr[i]==1){
                i++;
                continue;
            }
            int min=getMin(i);
            remove(i,min);
        }
        System.out.println(count);
    }

    public static int getMin(int start){
        int min=start;
        for (int i = start; i < arr.length; i++) {
            if (arr[i]==1){
                return min;
            }
            if (arr[i]>1){
                min=i;
            }
        }
        return min;
    }
    public static void remove(int first,int end){
        for (int i=first;i<=end;i++){
            arr[i]--;
        }
        count++;
    }


}
