package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：最后一块石头重量
 * @Date：2025/6/25 22:46
 * @Filename：最后一块石头重量
 */
public class 最后一块石头重量 {

    public static void main(String[] args) {
        int[] stones = {2,7,4,1,8,1};
        System.out.println(lastStoneWeightII(stones));
    }
    public static int lastStoneWeightII(int[] stones) {
        int sum=0;
        for (int stone : stones) {
            sum+=stone;
        }
        int target=sum/2;
        int near=near(stones,target);
        return sum-2*near;
    }
    public static int near(int[] stones,int target){
        int[][]dp=new int[stones.length+1][target+1];
        for(int i=1;i<=stones.length;i++){
            for (int j=0;j<=target;j++){
                dp[i][j]=dp[i-1][j];
                if (j>=stones[i-1]){
                    dp[i][j]=Math.max(dp[i][j],dp[i-1][j-stones[i-1]]+stones[i-1]);
                }
            }
        }
        return dp[stones.length][target];
    }
}
