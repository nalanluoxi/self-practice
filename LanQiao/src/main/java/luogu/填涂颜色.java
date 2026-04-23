package luogu;

import java.util.Scanner;

public class 填涂颜色 {
    public static void main(String[] args) {
        yanse();
    }


    static int[][] arr;
    static Scanner scanner = new Scanner(System.in);

    public static void yanse() {
        int n = scanner.nextInt();
        arr = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                arr[i][j] = scanner.nextInt();
            }
        }

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if (isIn(i,j)){
                    arr[i][j]=2;
                }
            }
        }

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                System.out.print(arr[i][j]+" ");
            }
            System.out.println();
        }


    }

    public static boolean isIn(int x, int y) {
        if (arr[x][y]!=0){
            return false;
        }
        int tempx = x;
        while (true) {
            if (tempx < 0) {
                return false;
            }
            if (arr[tempx][y] != 0) {
                break;
            }
            tempx--;
        }
        tempx = x;
        while (true) {
            if (tempx == arr.length) {
                return false;
            }
            if (arr[tempx][y] != 0) {
                break;
            }
            tempx++;
        }
        int tempy = y;
        while (true) {
            if (tempy < 0) {
                return false;
            }
            if (arr[x][tempy] != 0) {
                break;
            }
            tempy++;
        }
        tempy = y;
        while (true) {
            if (tempy == arr[0].length) {
                return false;
            }
            if (arr[x][tempy] != 0) {
                break;
            }
            tempy++;
        }

        return true;
    }

}
