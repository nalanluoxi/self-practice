package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：补给站问题
 * @Date：2025/1/16 20:15
 * @Filename：补给站问题
 */
public class 补给站问题 {


    public static void main(String[] args) {
        // Add your test cases here

        System.out.println(solution(4, 3, new int[][]{{0,3},{2,2},{3,1}}) == 9);
    }

  /* *//* public static int solution(int m, int n ,int[][] p) {
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = p[i][1];
        }
        int allMoney = 0;
        int minMoney=nums[0];
        int minIndex=0;
        for (int i = 1; i < n; i++) {
            if (nums[i]<minMoney){
                allMoney+=minMoney*(i-minIndex);
                minIndex=i;
                minMoney=nums[i];
            }
        }
        allMoney+=minMoney*(m-minIndex);
        return allMoney;
    }*/
   public static int solution(int m, int n, int[][] p) {
       assert n == p.length && m >= p[p.length - 1][0] && p[0][0] == 0;
       int cost = 0;
       int minPrice = p[0][1];
       int minPriceIndex = 0;

       for (int j = 1; j < n; j++) {
           int a = p[j][0];
           int b = p[j][1];
           if (b < minPrice) {
               cost += minPrice * (a - minPriceIndex);
               minPrice = b;
               minPriceIndex = a;
           }
       }

       cost += minPrice * (m - minPriceIndex);
       return cost;
   }
}
