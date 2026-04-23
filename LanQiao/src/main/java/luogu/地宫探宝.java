import java.util.Arrays;
import java.util.Scanner;

public class 地宫探宝{
    static int N;
    static int M;
    static int K;
    static int mod = 1000000007;
    // c[x][y]用来记录地宫坐标
    static int[][] c = new int[55][55];
    // dp[x][y][count][max_value]表示的是从起点到(x,y)坐标下，获得count件宝物的路径有多少条
    // max_value表示当前手中宝物最大价值
    static int[][][][] dp = new int[55][55][101][15];

    public static void main(String[] args) {
        // 初始化四维数组dp，全部赋值为-1，因为宝物的价值可以为0
        initdp(dp, -1);

        Scanner s1 = new Scanner(System.in);
        Scanner s2 = new Scanner(System.in);

        N = s1.nextInt();
        M = s1.nextInt();
        K = s1.nextInt();

        System.out.println("请输入" + N + "行" + M + "列" + "的迷宫");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                c[i][j] = s2.nextInt();
               // c[i][j]++;
            }
        }
        System.out.println("共有" + dfs(0, 0, 0, 0) + "条路径");
    }

    public static void initdp(int[][][][] dp, int num) {
        for (int i = 0; i < 55; i++) {
            for (int j = 0; j < 55; j++) {
                for (int k = 0; k < 101; k++) {
                    for (int m = 0; m < 55; m++)
                        // 注意fill方法的使用，第一个参数必须为一维数组，笔者在这里摔过，特此填坑
                        Arrays.fill(dp[i][j][k], num);
                }
            }
        }
    }

    public static int dfs(int x, int y, int count, int v) {
        if (dp[x][y][count][v] != -1) {// 如果在c[x][y]处已经走过，则后面不再经过这里
            return dp[x][y][count][v];
        }
        if (x == N - 1 && y == M - 1) {// 如果到达出口
            if (count == K || (count == K - 1 && c[x][y] > v)) {// 如果手中宝物数量为k或者手中宝物数量为k-1
                // 且当前位置的宝物价值大于手中宝物的最大价值
                return dp[x][y][count][v] = 1; // 可选路径加1
            }
            return dp[x][y][count][v] = 0; // 否则路径条数不变
        }
        int t = 0;// t用于记录路径数除以mod的余数

        // 判断还没到地宫尽头的情况
        if (y + 1 < M) {
            if (c[x][y] > v) {
                t = (t + dfs(x, y + 1, count + 1, c[x][y])) % mod;
            }
            t = (t + dfs(x, y + 1, count, v)) % mod;
        }
        if (x + 1 < N) {
            if (c[x][y] > v) {
                t = (t + dfs(x + 1, y, count + 1, c[x][y])) % mod;
            }
            t = (t + dfs(x + 1, y, count, v)) % mod;
        }

        return dp[x][y][count][v] = t % mod;
    }
}
