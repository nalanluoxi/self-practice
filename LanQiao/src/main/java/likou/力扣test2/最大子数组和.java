package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：最大子数组和
 * @Date：2025/6/6 11:00
 * @Filename：最大子数组和
 */
public class 最大子数组和 {

    public static void main(String[] args) {
        System.out.println(maxSubArray(new int[]{-2,1,-3,4,-1,2,1,-5,4}));
        //System.out.println(maxSubArray(new int[]{1}));
    }

    /*    public static int maxSubArray(int[] nums) {
            int[]dp=new int[nums.length+1];
            dp[0]=nums[0];
            int ans=dp[0];
            for (int i = 1; i < nums.length; i++) {
                dp[i]= Math.max(nums[i],nums[i]+dp[i-1]);
                ans=Math.max(dp[i],ans);
            }
            return ans;
        }*/
    public static int maxSubArray(int[] nums) {
        int[] dp = new int[nums.length];
        dp[nums.length - 1] = nums[nums.length - 1];
        int ans = nums[nums.length - 1];
        for (int i = nums.length - 2; i >= 0; i--) {
            dp[i] = Math.max(nums[i], nums[i] + dp[i + 1]);
            ans = Math.max(ans, dp[i]);
        }
        return ans;
    }
}
