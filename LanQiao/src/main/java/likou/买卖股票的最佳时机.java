package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：买卖股票的最佳时机
 * @Date：2025/3/24 16:32
 * @Filename：买卖股票的最佳时机
 */
public class 买卖股票的最佳时机 {
    public static void main(String[] args) {
        int[] prices={7,1,5,3,6,4};
        System.out.println(maxProfit(prices));
    }

    public static int maxProfit(int[] prices) {
        int ans=0;
        int min=prices[0];
        for (int i = 0; i < prices.length; i++) {
            if (prices[i]<min){
                min=prices[i];
            }
            int temp = prices[i] - min;
            ans=Math.max(ans,temp);
        }
        return ans>0?ans:0;
    }
}
