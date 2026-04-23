package luogu;

import java.util.Scanner;

public class 交换瓶子 {
    public static void main(String[] args) {
        pingzi();
    }

    public static void swap(int i,int j){
        int tem=arr[i];
        arr[i]=arr[j];
        arr[j]=tem;
    }

    public static int[] arr;
    public static void pingzi(){
        Scanner scanner=new Scanner(System.in);
        int n= scanner.nextInt();
        arr=new int[n];
        int count=0;
        for (int i = 0; i < n; i++) {
            arr[i]= scanner.nextInt();
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i]==(i+1)){
                continue;
            }
            for (int j = i+1; j <arr.length ; j++) {
                if (arr[j]==(i+1)){
                    swap(j,i);
                    count++;
                }
            }
        }
        System.out.println(count);

    }


}
