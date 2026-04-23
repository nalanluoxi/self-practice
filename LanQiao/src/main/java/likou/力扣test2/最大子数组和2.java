package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：最大子数组和2
 * @Date：2025/6/17 17:18
 * @Filename：最大子数组和2
 */
public class 最大子数组和2 {
    public static void main(String[] args) {
        int[] nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println(maxSubArray(nums));
    }
    public static int maxSubArray(int[] nums) {
        int len=nums.length;
        int []dp=new int [len];
        dp[len-1]=nums[len-1];
        int ans=dp[len-1];
        for(int i=len-2;i>=0;i--){
            dp[i]=Math.max(nums[i],nums[i]+dp[i+1]);
            ans=Math.max(dp[i],ans);
        }
        return ans;
    }
}
