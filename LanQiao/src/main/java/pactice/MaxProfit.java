package pactice;

public class MaxProfit {
    public static void main(String[] args) {

        int[]prices={1,2,3,4,5};
        maxProfit(prices);
    }

    public static int maxProfit(int[] prices) {
        int maxprice=0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i]-prices[i-1]>0){
                maxprice+=(prices[i]-prices[i-1]);
            }
        }
      //  System.out.println(maxprice);
        return maxprice;
    }

}
