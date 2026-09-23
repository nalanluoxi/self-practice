package 面试;

import java.util.Scanner;

public class Test0806 {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        Scanner scanner=new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int[][]arr=new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                arr[i][j]=scanner.nextInt();
            }
        }

        test(arr);


    }

    public static void test(int[][]arr){
        int[][]dp=new int[arr.length][arr[0].length];
        int n = arr.length;
        int m = arr[0].length;
        int l=0;
        int w=0;
        int max=Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            if (i==0){
                continue;
            }else {
                for (int j = 0; j < m; j++) {
                    if (dp[i-1][j]>0){
                        dp[i][j]+=dp[i-1][j];
                    }
                }
            }
            for (int j = 0; j < m; j++) {
                if (j==0){
                    dp[i][j]=arr[i][j];
                }else {
                    if (dp[i][j-1]+arr[i][j]>0){
                        dp[i][j]=arr[i][j]+dp[i][j-1];
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (dp[i][j]>max){

                }
            }
        }
    }
}
