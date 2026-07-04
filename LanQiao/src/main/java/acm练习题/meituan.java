package acm练习题;

import java.util.Map;
import java.util.Scanner;
public class meituan {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        for (int i = 0; i < T; i++) {
            int n = in.nextInt();
            int[][]arr = new int[n][2];
            for (int j = 0; j < n; j++) {
                arr[j][0]=in.nextInt();
                arr[j][1]=in.nextInt();
            }
            test(arr);
            //test2(arr);

        }
    }

/*
    public static void test2(int[][]nums){
        int len = nums.length;
        int[][] arr=new int[len][len];
        int[][][]dp=new int[len][len][len];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                for (int k = 0; k < len; k++) {
                    dp[i][j][k]=-1;
                }
            }
        }
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (i==j){
                    continue;
                }
                //圆心是点i rlen是点ij距离，排除点i，j的点
                for (int k = 0; k < len; k++) {
                    if (k==i||k==j){
                        continue;
                    }
                    if (getIn(nums,i,j,k,dp)==1){
                        arr[i][j]++;
                    }
                }

            }
        }



        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                System.out.print(arr[i][j]+" ");
            }
            System.out.println();
        }


    }

    public static int getIn(int[][]arr,int ni,int nj,int t,int[][][]dp){
        if (dp[ni][nj][t]!=-1){
            return dp[ni][nj][t];
        }

        int ix=arr[ni][0];
        int iy=arr[ni][1];
        int jx=arr[nj][0];
        int jy=arr[nj][1];
        //半径平方
        int rlen2 = (ix - jx) * (ix - jx) + (iy - jy) * (iy - jy);
        if (t==ni||t==nj){
            return 0;
        }
        int[] node = arr[t];
        int x=node[0];
        int y=node[1];
        int len2node=(x-ix)*(x-ix)+(y-iy)*(y-iy);
        if (len2node<=rlen2){
            dp[ni][nj][t]=1;
            return 1;
        }
        return 0;
    }*/

    public static int getNum(int[][]arr,int ni,int nj){
        int ans=0;
        int ix=arr[ni][0];
        int iy=arr[ni][1];
        int jx=arr[nj][0];
        int jy=arr[nj][1];
        //半径平方
        int rlen2 = (ix - jx) * (ix - jx) + (iy - jy) * (iy - jy);
        for (int i = 0; i < arr.length; i++) {
            if (i==ni||i==nj){
                continue;
            }
            int[] node = arr[i];
            int x=node[0];
            int y=node[1];
            int len2node=(x-ix)*(x-ix)+(y-iy)*(y-iy);
            if (len2node<=rlen2){
                ans++;
               // System.out.println("条件匹配的点："+x+" "+y);
            }

        }


      //  System.out.println("ans: " +ans);
        return ans;
    }
    public static void test(int[][]nums){
        int len = nums.length;
        int[][] arr=new int[len][len];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (i==j){
                    continue;
                }
                //圆心是点i rlen是点ij距离，排除点i，j的点
                int num = getNum(nums, i, j);
                arr[i][j]=num;

            }
        }
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                System.out.print(arr[i][j]+" ");
            }
            System.out.println();
        }
    }



}
