package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：零钱兑换
 * @Date：2025/2/10 10:15
 * @Filename：零钱兑换
 */
public class 零钱兑换 {
    public static void main(String[] args) {
        System.out.println(coinChange(new int[]{186, 419, 83, 408}, 6249));
    }


    static int[] coins;
    static int[] visited;
/*    public static int coinChange(int[] c, int amount) {
        if (c.length==0){
            return -1;
        }
        coins=c;
        visited=new int[amount+1];
        return dps(amount);
    }  */

    public static int coinChange(int[] c, int amount) {
        if (c.length == 0) {
            return -1;
        }
        coins = c;
        visited = new int[amount + 1];
        for (int i = 1; i <= amount; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 0; j < coins.length; j++) {
                if (i-coins[j]>=0&&visited[i-coins[j]]<min){
                    min=visited[i-coins[j]]+1;
                }
            }
            visited[i]=min;
            //visited[i]=(min==Integer.MAX_VALUE?-1:min);
        }
        return visited[amount]==Integer.MAX_VALUE?-1:visited[amount];
    }


    public static int dps(int amount) {
        if (amount < 0) {
            return -1;
        }
        if (amount == 0) {
            return 0;
        }
        if (visited[amount] != 0) {
            return visited[amount];
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < coins.length; i++) {
            int res = dps(amount - coins[i]);
            if (res >= 0 && res < min) {
                min = res + 1;
            }
        }
        visited[amount] = (min == Integer.MAX_VALUE ? -1 : min);
        return visited[amount];
    }
}
