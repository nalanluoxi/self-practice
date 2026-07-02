package acm练习题;

import java.util.Arrays;
import java.util.Scanner;

public class Test0702 {

/*
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // 读取输入
        int n = in.nextInt();
        int[] a = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = in.nextInt();
        }

        test1(n, a);
    }


    public static void test1(int n, int[] a){
        // DP状态定义：dp[i][j] 表示前i只怪物中，击败数量%10=j时的最大经验值
        long[][] dp = new long[n + 1][10];

        // 初始化：所有状态设为-1（不可达）
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j < 10; j++) {
                dp[i][j] = -1;
            }
        }

        // 初始状态：0只怪物，击败0只，经验值为0
        dp[0][0] = 0;

        // 状态转移
        for (int i = 1; i <= n; i++) {           // 遍历每只怪物（修正：i <= n）
            for (int j = 0; j < 10; j++) {       // 遍历所有可能的击败数%10的状态
                if (dp[i - 1][j] == -1) continue; // 如果前置状态不可达，跳过

                long curExp = dp[i - 1][j];      // 当前已获得的经验值

                // 选择1：放走第i只怪物
                // 获得i点经验，击败数量不变（j不变）
                dp[i][j] = Math.max(dp[i][j], curExp + i);

                // 选择2：击败第i只怪物
                // 击败数量+1，所以j变成(j+1)%10
                int nextJ = (j + 1) % 10;
                // 收益 = 基础经验a[i] + 额外奖励((j+1)%10 * a[i])
                long bonus = (long)((j + 1) % 10) * a[i];
                long exp2 = curExp + a[i] + bonus;
                dp[i][nextJ] = Math.max(dp[i][nextJ], exp2);
            }
        }

        // 答案：在所有可能的最终状态中取最大值
        long ans = 0;
        for (int j = 0; j < 10; j++) {
            ans = Math.max(ans, dp[n][j]);
        }

        System.out.println(ans);
    }*/


    public static void main(String[] args) {
        test3In();
    }

    public static void test3In() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[][] stars = new int[n][3];
        for (int i = 0; i < n; i++) {
            stars[i][0] = in.nextInt(); // x
            stars[i][1] = in.nextInt(); // y
            stars[i][2] = in.nextInt(); // z
        }
        test3(stars);
    }

    public static void test3(int[][]arr){
        int n = arr.length;
        Arrays.sort(arr,(a,b)->{
            if (a[0]!=b[0])return a[0]-b[0];
            else if (a[1]!=b[1]) {
                return a[1]-b[1];
            }else {
                return a[2]-b[2];
            }
        });
        boolean []visited=new boolean[n];
        int nums=0;
        for (int i = 0; i < n; i++) {
            if (visited[i]){
                continue;
            }
            for (int j = i+1; j < n; j++) {
                if (arr[i][0]<arr[j][0]&& arr[i][1]<arr[j][1] && arr[i][2]<arr[j][2]){
                    visited[j]=true;
                    visited[i]=true;
                    nums++;
                    break;
                }
            }
        }
        System.out.println(nums);
    }
/*

    static final int MOD = 998244353;

    // 快速幂算法：计算 base^exp % MOD
    public static int modPow(int base, int exp) {
        int result = 1;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                result = (int) ((long) result * base % MOD);
            }
            base = (int) ((long) base * base % MOD);
            exp >>= 1;
        }
        return result;
    }

    // 预处理逆元
    public static void precomputeInverses(int n, int[] inv) {
        inv[1] = 1;
        for (int i = 2; i <= n; ++i) {
            inv[i] = (int) ((long) (MOD - MOD / i) * inv[MOD % i] % MOD);
        }
    }

    // 计算系数数组
    public static int[] computeCoefficients(int n, int[] inv) {
        int[] c = new int[2 * n + 1];
        Arrays.fill(c, 0);

        c[0] = modPow(2, n);

        if (n == 0) return c;

        c[1] = (int) ((long) n * c[0] % MOD * inv[2] % MOD);

        for (int j = 1; j < 2 * n; ++j) {
            long term1 = (long) (6L * n - 3L * j + 3) * c[j - 1] % MOD;
            long term2 = (long) (n - j) * c[j] % MOD;
            long numerator = (term1 + term2) % MOD;
            if (numerator < 0) numerator += MOD;
            c[j + 1] = (int) (numerator * inv[2 * (j + 1)] % MOD);
        }

        return c;
    }

    // 计算最终答案
    public static int computeAnswer(int[] c, int n, int m) {
        int ans = 0;
        int lo = Math.max(0, n - m);
        int hi = Math.min(2 * n, n + m);
        for (int j = lo; j <= hi; ++j) {
            ans = (ans + c[j]) % MOD;
        }
        return ans;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();

        int[] inv = new int[4 * n + 2];
        precomputeInverses(4 * n + 1, inv);

        int[] c = computeCoefficients(n, inv);
        System.out.println(computeAnswer(c, n, m));
    }
*/


}
