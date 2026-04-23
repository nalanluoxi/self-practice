package likou.贪心;

/**
 * @Author 纳兰洛熙
 * @Package：likou.贪心
 * @Project：LanQiaoBei
 * @name：买卖股票
 * @Date：2025/3/12 16:31
 * @Filename：买卖股票
 */
public class 买卖股票 {
    public static void main(String[] args) {
        int[] nums = {7, 1, 5, 3, 6, 4};
        System.out.println(maxProfit(nums));
    }
    public static int maxProfit(int[] prices) {
        int ans = 0;
        for (int i = 1; i < prices.length; i++) {
            ans +=Math.max(0,prices[i]-prices[i-1]);
        }
        return ans;
    }
}
