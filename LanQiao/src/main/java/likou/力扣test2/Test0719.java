package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：Test0719
 * @Date：2025/7/19 16:47
 * @Filename：Test0719
 */
public class Test0719 {
    public static void main(String[] args) {
        //System.out.println(rob(new int[]{2,3,2}));
        System.out.println(rob(new int[]{200,3,140,20,10}));
    }

    public static int rob(int[] nums) {
        if (nums.length<=0){
            return 0;
        } else if (nums.length == 1) {
            return nums[0];
        } else if (nums.length == 2) {
            return Math.max(nums[0],nums[1]);
        }
        int len = nums.length;
        int[]dp=new int[len];
        return Math.max(help(nums,0,len-2, dp),
                help(nums,1,len-1, dp));
    }

    public static int help(int[]nums,int start,int end,int[]dp){
        for (int i = start; i <= end; i++) {
            dp[i]=nums[i];
            if (i>=start+2){
                dp[i]+=dp[i-2];
            }
        }
        return Math.max(dp[end],dp[end-1]);
    }
}
