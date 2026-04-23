package luogu;

import java.util.Scanner;

public class A_B数对 {
    public static void main(String[] args) {
        AB();
    }
    static Scanner scanner=new Scanner(System.in);

    public static void AB(){
        int N= scanner.nextInt();
        long c = scanner.nextLong();

        Long[] arr=new Long[N];
        for (int i = 0; i < arr.length; i++) {
            arr[i]= scanner.nextLong();
        }
        Long count= 0L;

        for (int i = 0; i < arr.length; i++) {
            Long A = arr[i];
            long target = c - A;
            int l=0,r=arr.length-1;
            while (l<=r){
                int mid = (r + l) / 2;
                if (mid==i){
                    mid++;
                }
                if (arr[mid]>target){
                    r=mid;
                } else if (arr[mid]<target) {
                    l=mid;
                } else if (arr[mid]==target) {
                    count++;
                    break;
                }
            }
        }
        System.out.println(count);

    }
}
