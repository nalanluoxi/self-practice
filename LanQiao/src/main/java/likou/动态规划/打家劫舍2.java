package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：打家劫舍2
 * @Date：2025/7/12 15:31
 * @Filename：打家劫舍2
 */
public class 打家劫舍2 {
    public static void main(String[] args) {
        int[]nums1={2,3,2};
        int[]nums={200,3,140,20,10};
        System.out.println(rob(nums));
    }

    public static int rob(int[] nums) {
        int length = nums.length;
        if (length == 1) {
            return nums[0];
        } else if (length == 2) {
            return Math.max(nums[0], nums[1]);
        }
        return Math.max(robRange(nums, 0, length - 2), robRange(nums, 1, length - 1));
    }

    public static int robRange(int[] nums, int start, int end){
        int len = end - start + 1;
        int[]dp=new int[len];
        dp[0]=nums[start];
        dp[1]=Math.max(dp[0],nums[start+1]);
        for (int i = 2; i < dp.length; i++) {
            dp[i]=Math.max(nums[i+start]+dp[i-2],dp[i-1]);
        }
        return dp[len-1];
    }
   /* public static int robRange(int[] nums, int start, int end) {
         int first = nums[start], second = Math.max(nums[start], nums[start + 1]);
        for (int i = start + 2; i <= end; i++) {
            int temp = second;
            second = Math.max(first + nums[i], second);
            first = temp;
        }
        return second;
    }*/




    public static int rob2(int[] nums) {
        int len = nums.length;
        if (len<=0){
            return 0;
        }
        if (len==1){
            return nums[0];
        }
        if (len==2){
            return Math.max(nums[0],nums[1]);
        }
        int ans=Integer.MIN_VALUE;
        int []dp=new int[len];
        for (int i = 0; i < len - 1; i++) {
            dp[i]=nums[i];
            if (i-2>=0){
                dp[i]+=dp[i-2];
            }
        }
        ans=Math.max(dp[len-2],dp[len-3]);
        for (int i = 1; i < len ; i++) {
            dp[i]=nums[i];
            if (i-2>0){
                dp[i]+=dp[i-2];
            }
        }
        ans=Math.max(ans,Math.max(dp[len-1],dp[len-2]));
        return ans;
    }
    public static int rob1(int[] nums) {
        int len = nums.length;
        int []dp=new int[len];
        dp[len -1]=nums[len-1];
        int max=Integer.MIN_VALUE;
        for (int i = len-2; i >=0; i--) {
            if (i+2<len){
                dp[i]=nums[i]+dp[i+2];
            }else {
                dp[i]=nums[i];
            }
            max=Math.max(max,dp[i]);
        }
        return max;
    }
}
