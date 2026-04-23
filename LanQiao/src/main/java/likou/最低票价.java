package likou;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：最低票价
 * @Date：2025/1/26 12:07
 * @Filename：最低票价
 */
public class 最低票价 {
    public static void main(String[] args) {
        int[] days = {1, 4, 6, 7, 8, 20};
        int[] costs = {2, 7, 15};
        int i = mincostTickets(days, costs);
        System.out.println(i);
    }

    public static int mincostTickets(int[] days, int[] costs) {
        day = days;
        cost = costs;
        // dp=new int[days.length];
        dp = new int[366];
        Arrays.fill(dp,Integer.MAX_VALUE);
        dp[days.length] = 0;
        // int res = f0n(0);
        //return f3();
        return f3();
    }

    static int[] day;
    static int[] cost;
    static int[] dp;
    static int[] choose = {1, 7, 30};
    /*public static int f0n(int i){
        if (i==day.length){
            return 0;
        }
        if (dp[i]!=Integer.MAX_VALUE){
            return dp[i];
        }
        int ans=Integer.MAX_VALUE;
        for (int k = 0,j=i; k < 3; k++) {
            while (j< day.length&&day[j]<day[i]+choose[k]){
                j++;
            }
            ans= Math.min(ans,f0n(j)+cost[k]);
        }
        dp[i]=ans;
        return ans;
    }*/

    public static int fn0() {
        int len = day.length;
        for (int i = len - 1; i >= 0; i--) {
            for (int k = 0, j = i; k < 3; k++) {
                while (j < day.length && day[j] < day[i] + choose[k]) {
                    j++;
                }
                dp[i] = Math.min(dp[i], dp[j] + cost[k]);
            }
        }
        return dp[0];
    }

    public static int f3(){
        int n= day.length;
        Arrays.fill(dp,0,n+1,Integer.MAX_VALUE);
        dp[n]=0;
        for (int i = n-1; i >=0 ; i--) {
            for (int k = 0,j=i; k < 3; k++) {
                while (j<day.length&&day[j]<day[i]+choose[k]){
                    j++;
                }
                dp[i]=Math.min(dp[i],dp[j]+cost[k]);
            }
        }
        return dp[0];
    }

}
