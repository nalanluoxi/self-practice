package likou;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：跳跃训练
 * @Date：2025/7/9 15:55
 * @Filename：跳跃训练
 */
public class 跳跃训练 {
    public static void main(String[] args) {
        System.out.println(trainWays(5));
    }
    static long mod=1000000007;


    public static int trainWays(int num) {
        if (num==0){
            return 1;
        }
        if (num<=2){
            return num;
        }
        int pre1=1;
        int pre2=2;
        for (int i = 2; i < num; i++) {
            int t = pre1 + pre2;
            t= (int) (t%mod);
            pre1=pre2;
            pre2=t;
        }
        return pre2;
    }

    public static int trainWays2(int num) {
        if (num==0){
            return 1;
        }
        if (num<=2){
            return num;
        }
        int[]dp=new int[num];
        dp[0]=1;
        dp[1]=2;
        for (int i = 2; i < num; i++) {
            int t = dp[i - 1] + dp[i - 2];
            dp[i]= (int) (t%mod);
        }
        return dp[num-1];
    }
    public int[] inventoryManagement(int[] stock, int cnt) {
        Arrays.sort(stock);
        int[]ans=new int[cnt];
        for (int i = 0; i < ans.length; i++) {
            ans[i]=stock[i];
        }
        return ans;
    }
}
