package acm练习题;

import java.util.*;

/**
 * @author nalan_luoxi
 * @version 1.0
 * @email adrian0304@qq.com
 * @project self-practice
 * @package acm练习题
 * @date 2026-06-21 10:31
 */
public class Test0621 {

    public static void main(String[] args) {
        test03();
    }

    // ==================== 迷宫寻宝 ====================

    // 上下左右顺序（题目要求按此顺序判断）
    static int[] dxs = {-1, 1, 0, 0};
    static int[] dys = {0, 0, -1, 1};

    public static void test03() {
        Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        while (T-- > 0) {
            int m = in.nextInt(), n = in.nextInt();
            in.nextLine();
            char[][] grid = new char[m][n];
            for (int i = 0; i < m; i++) {
                grid[i] = in.nextLine().toCharArray();
            }
            System.out.println(mazeSolve(grid, m, n));
        }
    }

    public static int mazeSolve(char[][] grid, int m, int n) {
        int sx = -1, sy = -1;
        int[][] tPos = new int[10][2];
        boolean[] has = new boolean[10];

        // 解析地图：找起点和所有宝箱位置
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                char c = grid[i][j];
                if (c == '*') {
                    sx = i; sy = j;
                } else if (c >= '0' && c <= '9') {
                    int k = c - '0';
                    tPos[k] = new int[]{i, j};
                    has[k] = true;
                }
            }
        }
        int total = 0;
        for (boolean b : has) if (b) total++;

        boolean[] collected = new boolean[10];
        int cx = sx, cy = sy, steps = 0, cnt = 0;
        int curTarget = -1;
        int[][] dist = null;

        while (cnt < total) {
            // 第1步：找目标（曼哈顿距离最小，相同取编号最小）
            int tk = -1, minD = Integer.MAX_VALUE;
            for (int k = 0; k < 10; k++) {
                if (!has[k] || collected[k]) continue;
                int d = Math.abs(tPos[k][0] - cx) + Math.abs(tPos[k][1] - cy);
                if (d < minD || (d == minD && k < tk)) {
                    minD = d; tk = k;
                }
            }

            // 目标变了才重新BFS（BFS从目标出发，得到所有格子到目标的最短距离）
            if (tk != curTarget) {
                curTarget = tk;
                dist = bfsFromTarget(grid, m, n, tPos[tk][0], tPos[tk][1]);
            }

            // 第2步：无法到达则返回-1
            if (dist[cx][cy] == Integer.MAX_VALUE) return -1;

            // 按上下左右顺序，找第一个能缩短距离的方向走一步
            if (dist[cx][cy] > 0) {
                for (int d = 0; d < 4; d++) {
                    int nx = cx + dxs[d], ny = cy + dys[d];
                    if (nx < 0 || nx >= m || ny < 0 || ny >= n) continue;
                    if (grid[nx][ny] == '#') continue;
                    if (dist[nx][ny] < dist[cx][cy]) {
                        cx = nx; cy = ny; steps++;
                        break;
                    }
                }
            }

            // 第3步：收集当前位置的宝箱（可能顺路收集非目标宝箱）
            for (int k = 0; k < 10; k++) {
                if (!has[k] || collected[k]) continue;
                if (tPos[k][0] == cx && tPos[k][1] == cy) {
                    collected[k] = true;
                    cnt++;
                    if (k == curTarget) curTarget = -1; // 下轮重新选目标
                }
            }
        }
        return steps;
    }

    /**
     * BFS从目标出发，计算目标到所有可达格子的最短距离
     * dist[i][j] = 格子(i,j)到目标的最短路径长度
     */
    public static int[][] bfsFromTarget(char[][] grid, int m, int n, int sx, int sy) {
        int[][] dist = new int[m][n];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
        Queue<int[]> q = new LinkedList<>();
        dist[sx][sy] = 0;
        q.offer(new int[]{sx, sy});
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0], y = cur[1];
            for (int d = 0; d < 4; d++) {
                int nx = x + dxs[d], ny = y + dys[d];
                if (nx < 0 || nx >= m || ny < 0 || ny >= n) continue;
                if (grid[nx][ny] == '#') continue;
                if (dist[nx][ny] == Integer.MAX_VALUE) {
                    dist[nx][ny] = dist[x][y] + 1;
                    q.offer(new int[]{nx, ny});
                }
            }
        }
        return dist;
    }

    // ==================== 斩击矩阵 ====================

    public static void test02() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        in.nextLine();
        int[][] arr = new int[n][n];
        for (int i = 0; i < n; i++) {
            String[] split = in.nextLine().split(" ");
            for (int j = 0; j < split.length; j++) {
                arr[i][j] = Integer.parseInt(split[j]);
            }
        }
        zhan(arr);
    }

    public static void zhan(int[][] arr) {
        if (arr.length == 0) return;

        int n = arr.length, m = arr[0].length;

        // 预计算行和与列和
        int[] rowSum = new int[n];
        int[] colSum = new int[m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                rowSum[i] += arr[i][j];
        for (int j = 0; j < m; j++)
            for (int i = 0; i < n; i++)
                colSum[j] += arr[i][j];

        // 枚举所有(r,c)组合，找最大得分（相同时取最小r，再最小c）
        int bestR = 0, bestC = 0, bestScore = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int score = rowSum[i] + colSum[j] - arr[i][j];
                if (score > bestScore) {
                    bestScore = score;
                    bestR = i;
                    bestC = j;
                }
            }
        }

        System.out.println((bestR + 1) + " " + (bestC + 1));
        int[][] newarr = getNewarr(arr, bestR, bestC);
        zhan(newarr);
    }

    public static int[][] getNewarr(int[][] arr, int lenIndex, int heiINdex) {
        int n = arr.length, m = arr[0].length;
        int[][] brr = new int[n - 1][m - 1];
        int ni = 0;
        for (int i = 0; i < n; i++) {
            if (i == lenIndex) continue;
            int nj = 0;
            for (int j = 0; j < m; j++) {
                if (j == heiINdex) continue;
                brr[ni][nj++] = arr[i][j];
            }
            ni++;
        }
        return brr;
    }
}