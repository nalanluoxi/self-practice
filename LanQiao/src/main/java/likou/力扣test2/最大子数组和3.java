package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：最大子数组和3
 * @Date：2025/6/26 17:30
 * @Filename：最大子数组和3
 */
public class 最大子数组和3 {
    public static void main(String[] args) {
        int[]nums={
                -2,1,-3,4,-1,2,1,-5,4
        };
        System.out.println(maxSubArray(nums));
    }


/*    public static int maxSubArray(int[] nums) {
        if (nums.length==0){
            return 0;
        }
        int ans=nums[0];
        int sum=0;
        for (int num : nums) {
            sum=Math.max(num,sum+num);
            ans=Math.max(ans,sum);
        }
        return ans;
    }*/

    static int[] dp;
    public static int maxSubArray(int[] nums) {
        int n = nums.length;
        dp=new int[n];
        dp[0]=nums[0];
        int ans=nums[0];
        for (int i = 1; i < n; i++) {
            dp[i]=Math.max(nums[i],dp[i-1]+nums[i]);
            ans=Math.max(ans,dp[i]);
        }
        return ans;
    }
}
