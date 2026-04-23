package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：l连续天数的最高销售额
 * @Date：2025/6/25 23:20
 * @Filename：l连续天数的最高销售额
 */
public class l连续天数的最高销售额 {
    public static void main(String[] args) {
        int[] sales2 = {-1};
        int[] sales3 = {-2,1};
        int[] sales = {-2,1,-3,4,-1,2,1,-5,4};
        System.out.println(maxSales(sales));
    }

    public static int maxSales(int[] sales) {
        if (sales.length==1){
            return sales[0];
        }
        int ans=sales[0];
        int sum=sales[0];
        for (int i=1;i<sales.length;i++) {
            int sale=sales[i];
            sum=Math.max(sale,sum+sale);
            ans=Math.max(ans,sum);
        }
        return ans;
    }

  /*  public static int maxSales(int[] sales) {
        if (sales.length==0){
            return 0;
        } else if (sales.length==1) {
            return sales[0];
        }
        int n = sales.length;
        int[][]dp=new int[n+1][n+1];
        int ans=Integer.MIN_VALUE;
        for (int i = 1; i <= n; i++) {
            dp[i][i]=sales[i-1];
            ans=Math.max(ans,dp[i][i]);
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <=n ; j++) {
                if (i==j){
                    continue;
                }
                dp[i][j]=dp[i][j-1]+sales[j-1];
                ans=Math.max(ans,dp[i][j]);
            }
        }
        return ans;
    }*/
}
