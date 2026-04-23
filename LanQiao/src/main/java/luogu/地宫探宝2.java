package luogu;

import java.util.Arrays;
import java.util.Scanner;

public class 地宫探宝2 {

    public static void main(String[] args) {
        digong();
    }

    static int N;
    static int M;

    static int K;
    static int[][] gong = new int[60][60];
    static int[][][][] dp = new int[55][55][15][15];


    public static void digong() {
        Scanner scanner=new Scanner(System.in);
        N= scanner.nextInt();
        M= scanner.nextInt();
        K= scanner.nextInt();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                gong[i][j]= scanner.nextInt();
            }
        }
        for (int i = 0; i < 55; i++) {
            for (int j = 0; j < 55; j++) {
                for (int k = 0; k < 101; k++) {
                    for (int m = 0; m < 55; m++)
                        // 注意fill方法的使用，第一个参数必须为一维数组，笔者在这里摔过，特此填坑
                        Arrays.fill(dp[i][j][k], -1);
                }
            }
        }
        System.out.println(dfs(0,0,0,0));
    }

    public static int dfs(int x,int y,int count ,int max){
        int t=0;

        if (x+1<N){
            if (max<gong[x][y]){
                t+=dfs(x+1,y,count+1,gong[x][y]);
            }
            t+=dfs(x+1,y,count,max);
        }

        if (y+1<M){
            if (max<gong[x][y]){
                t+=dfs(x,y+1,count+1,gong[x][y]);
            }
            t+=dfs(x,y+1,count,max);
        }

        return dp[x][y][count][max]=t;
    }
}
