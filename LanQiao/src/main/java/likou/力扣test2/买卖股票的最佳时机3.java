package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：买卖股票的最佳时机3
 * @Date：2025/6/27 17:30
 * @Filename：买卖股票的最佳时机3
 */
public class 买卖股票的最佳时机3 {
    public static void main(String[] args) {
        int[]prices={7,1,5,3,6,4};
        System.out.println(maxProfit(prices));
    }


    public static int maxProfit(int[] prices) {
        int ans=0;
        int max=prices[prices.length-1];
        for (int i = prices.length-2; i >=0 ; i--) {
            ans=Math.max(max - prices[i],ans);
            max=Math.max(max,prices[i]);
        }
        return ans;
    }
}
