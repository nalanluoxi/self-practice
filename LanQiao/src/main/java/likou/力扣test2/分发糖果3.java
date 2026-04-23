package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：分发糖果3
 * @Date：2025/7/13 10:38
 * @Filename：分发糖果3
 */
public class 分发糖果3 {
    public static void main(String[] args) {
        int[]nums1={1,0,2};
        int[]nums2={1,2,2};
        int[]nums={1,2,87,87,87,2,1};
        System.out.println(candy(nums));
    }

    public static int candy(int[] nums) {
        int len = nums.length;
        int[]dp=new int[len];
        for (int i = 0; i < len; i++) {
            dp[i]=1;
            if (i-1>=0&&nums[i]>nums[i-1]){
                dp[i]=Math.max(dp[i]+1,dp[i-1]+1);
            }
        }
        for (int i = len-1; i >= 0; i--) {
            if (i+1<len && nums[i]>nums[i+1]){
                dp[i]=Math.max(dp[i],dp[i+1]+1);
            }
        }
        int ans=0;
        for (int i : dp) {
            ans+=i;
        }
        return ans;
    }
}
