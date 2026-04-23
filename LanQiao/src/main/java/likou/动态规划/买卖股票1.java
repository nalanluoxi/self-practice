package likou.动态规划;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：买卖股票1
 * @Date：2025/3/12 18:15
 * @Filename：买卖股票1
 */
public class 买卖股票1 {
    public static void main(String[] args) {
       // int[] nums = {7,6,4,3,1};
        int[] nums = {7,1,5,3,6,4};
        System.out.println(maxProfit(nums));
    }


    public static int maxProfit(int[] prices) {
        int [][]dp=new int[prices.length][2];
        dp[0][0]=-prices[0];
        dp[0][1]=0;
        for (int i = 1; i < prices.length; i++) {
            dp[i][0]=Math.max(dp[i-1][0],-prices[i]);
            dp[i][1]=Math.max(dp[i-1][1],dp[i-1][0]+prices[i]);
        }
        return dp[prices.length-1][1];
    }
    /*public static int maxProfit(int[] prices) {
        int ans=0;
        int min =Integer.MAX_VALUE;
        for (int i = 1; i < prices.length; i++) {
            min=Math.min(min,prices[i]);
            ans=Math.max(ans,prices[i]-min);
        }
        return ans;
    }*/
    /*public static int maxProfit(int[] prices) {
        int ans=0;
        Deque<Integer> deque = new LinkedList<>();
        for (int i = 0; i < prices.length; i++) {
            while (!deque.isEmpty() && prices[i]<=prices[deque.peekLast()]){
                Integer last = deque.pollLast();
                int left = deque.isEmpty() ? last : deque.peekFirst();
                ans=Math.max(ans,prices[last]-prices[left]);
            }
            deque.addLast(i);
        }
        while (!deque.isEmpty()){
            Integer last = deque.pollLast();
            int left = deque.isEmpty()? last : deque.peekFirst();
            ans=Math.max(ans,prices[last]-prices[left]);
        }

        return ans;
    }*/
}
