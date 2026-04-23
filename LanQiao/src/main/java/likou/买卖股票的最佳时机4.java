package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：买卖股票的最佳时机4
 * @Date：2025/5/22 20:04
 * @Filename：买卖股票的最佳时机4
 */
public class 买卖股票的最佳时机4 {
    public static void main(String[] args) {
        int[] prices = {3,2,6,5,0,3};
        int k = 2;
        int i = maxProfit(k, prices);
        System.out.println(i);
    }

    public static int maxProfit(int k, int[] prices) {
        int len = prices.length;
        int[][]dp=new int[len][2];
        dp[len-1][0]=prices[len-1];
        dp[len-1][1]=len-1;
        for (int i = len-2; i >=0; i--) {
            if (prices[i]<=dp[i+1][0]){
                dp[i][0]=prices[i];
                dp[i][1]=i;
            }else {
                dp[i][0]=dp[i+1][0];
                dp[i][1]=dp[i+1][1];
            }
        }
        int [] tans=new int[len];
        int count=0;
        int ans=0;
        for (int i = len-1; i >=0; i--) {
            tans[i]=prices[i]-dp[i][0];
            if (tans[i]<0){
                continue;
            }
            count++;
            int index=dp[i][1];
            while (count<k){
                int t = prices[index] - dp[index][0];
                if (t<0){
                    index++;
                    continue;
                }
                tans[i]+=t;
                count++;
                index=dp[index][1];
            }
            ans=Math.max(ans,tans[i]);
        }

        return ans;
    }
}
