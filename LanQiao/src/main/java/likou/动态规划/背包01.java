package likou.动态规划;

import 设计模式.结构模式.代理模式.JDKProxFactory;

import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：背包01
 * @Date：2025/6/22 22:28
 * @Filename：背包01
 */
public class 背包01 {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        int V=scanner.nextInt();//总时间
        int N=scanner.nextInt();//总草药数量
        int []v=new int[V];
        int []w=new int[V];
        for (int i = 0; i < N; i++) {
            v[i]=scanner.nextInt();//时间
            w[i]=scanner.nextInt();//价值
        }
        System.out.println(beibao(N,V,v,w));
    }

    public static int beibao(int N, int V, int[] t, int[] v) {
        int[]dp=new int[V+1];
        for (int i=1;i<=N;i++){
            for(int j=V;j>=t[i-1];j--){
                dp[j]=Math.max(dp[j],dp[j-t[i-1]]+v[i-1]);
            }
        }
        return dp[V];
    }
    /*public static int beibao2(int N,int V,int []工作总结2.0.md,int[]w){

        int[][]dp=new int[N+1][V+1];
        for(int i=1;i<=N;i++){
            for(int j=0;j<=V;j++){
                dp[i][j]=dp[i-1][j];
                if(j>=工作总结2.0.md[i-1]){
                    dp[i][j]=Math.max(dp[i][j],dp[i-1][j-工作总结2.0.md[i-1]]+w[i-1]);
                }
            }
        }
        return dp[N][V];
    }*/

   /* public static int beibao(int T, int M, int[] t, int[] 工作总结2.0.md) {
        //时间背包容量  总草药数量（数组长度  时间数组  价值数组
        int[][] dp = new int[M + 1][T + 1];

        for (int i = 1; i <= M; i++) {
            for (int j = 0; j <= T; j++) {
                // 不选第i个物品
                dp[i][j] = dp[i-1][j];
                // 选第i个物品（前提是背包容量足够）
                if (j >= t[i-1]) {
                    dp[i][j] = Math.max(dp[i][j], dp[i-1][j-t[i-1]] + 工作总结2.0.md[i-1]);
                }l
            }
        }
        return dp[M][T];
    }
*/

}
