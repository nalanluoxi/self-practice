package likou.动态规划;

import java.util.Scanner;

public class text {
    static final int maxn = 110000;
    static int n;
    static int[] val = new int[maxn];
    static Edge[] edges = new Edge[maxn << 1];
    static int[] head = new int[maxn];
    static int cnt;
    static int[][] f = new int[maxn][2];

    static class Edge {
        int next, to;

        Edge(int next, int to) {
            this.next = next;
            this.to = to;
        }
    }

    public static void add(int from, int to) {
        edges[++cnt] = new Edge(head[from], to);
        head[from] = cnt;
    }

    public static void dfs(int u, int fa) {
        for (int i = head[u]; i != 0; i = edges[i].next) {
            int v = edges[i].to;
            if (v == fa) continue;
            dfs(v, u);
            f[u][0] += Math.max(f[v][0], f[v][1]);
            f[u][1] += f[v][0];
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        for (int i = 1; i <= n; ++i) {
            val[i] = sc.nextInt();
            f[i][1] = val[i];
        }
        for (int i = 1; i < n; ++i) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            add(u, v);
            add(v, u);
        }
        dfs(1, 0);
        System.out.println(Math.max(f[1][0], f[1][1]));
        sc.close();
    }
}