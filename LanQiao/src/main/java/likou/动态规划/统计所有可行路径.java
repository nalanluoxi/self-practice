package likou.动态规划;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：统计所有可行路径
 * @Date：2025/6/22 21:19
 * @Filename：统计所有可行路径
 */
public class 统计所有可行路径 {
    public static void main(String[] args) {
        int []locations=new int[]{2,3,6,8,4};
        System.out.println(countRoutes(locations,1,3,5));
    }

    public static int countRoutes(int[] locations, int start, int finish, int fuel) {


        return 0;
    }


    public static int dfs(){


        return 0;
    }
 /*   static int[][]dp;
    static int []list;
    public static int countRoutes(int[] locations, int start, int finish, int fuel) {
        int n = locations.length;
        dp=new int[n][fuel+1];
        list=locations;
        for(int[] list:dp){
            Arrays.fill(list, -1);
        }
        return dfs(list,start,finish,fuel);
    }

    public static int dfs1(int start,int finish,int fuel){
        if(fuel<0){
            return 0;
        }
        if(dp[start][finish]!=-1){
            return dp[start][finish];
        }

        dp[start][fuel]=0;
        if (Math.abs(list[start]-list[finish])>fuel){
            return 0;
        }
        int n=list.length;
        for (int i = 0; i < n; i++) {
            if (start!=i){
                int c;
                if ((c=Math.abs(list[start]-list[i]))<=fuel){
                    dp[start][fuel]+=dfs1(i,finish,fuel-c);
                    dp[start][fuel]%=1000000007;
                }
            }
        }
        if (start==finish){
            dp[start][finish]++;
            dp[start][finish]%=1000000007;
        }
        return dp[start][finish];
    }

    static final int MOD = 1000000007;

    public static int dfs(int[] locations, int pos, int finish, int rest) {
        if (dp[pos][rest] != -1) {
            return dp[pos][rest];
        }

        dp[pos][rest] = 0;
        if (Math.abs(locations[pos] - locations[finish]) > rest) {
            return 0;
        }

        int n = locations.length;
        for (int i = 0; i < n; ++i) {
            if (pos != i) {
                int cost;
                if ((cost = Math.abs(locations[pos] - locations[i])) <= rest) {
                    dp[pos][rest] += dfs(locations, i, finish, rest - cost);
                    dp[pos][rest] %= MOD;
                }
            }
        }
        if (pos == finish) {
            dp[pos][rest] += 1;
            dp[pos][rest] %= MOD;
        }
        return dp[pos][rest];
    }*/


}
