package 蓝桥杯真题.省14A组;

import example.java2.IFo;

import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.省14A组
 * @Project：LanQiaoBei
 * @name：平均
 * @Date：2025/3/26 11:33
 * @Filename：平均
 */
public class 平均 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] arr = new int[n];
        int[] brr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
            brr[i] = scanner.nextInt();
        }
        avg(arr, brr);
    }

    static int[] arr;
    static int[] brr;
    static int[] hash;
    static int targetNum;


    public static void avg(int[] arrs, int[] brrs) {
        arr = arrs;
        brr = brrs;
        hash = new int[10];
        initHahs();
        targetNum=arrs.length/10;
        int ans=0;
        for (int i = 0; i < hash.length; i++) {
            while (hash[i]>targetNum){
                ans+= low(i);
            }
        }
        System.out.println(ans);
    }

    public static int low(int num){
        int minValue=Integer.MAX_VALUE;
        int minIndex=-1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i]==num){
                if (minValue>brr[i]){
                    minValue=brr[i];
                    minIndex=i;
                }
            }
        }
        hash[num]--;
        arr[minIndex]=-1;
        //System.out.println("min value :" +minValue);
        return minValue;
    }

    public static void initHahs() {
        for (int i = 0; i < arr.length; i++) {
            hash[arr[i]]++;
        }
    }

}
