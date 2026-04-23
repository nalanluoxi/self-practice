package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：成绩最大子数组
 * @Date：2025/4/10 17:36
 * @Filename：成绩最大子数组
 */
public class 成绩最大子数组 {
    public static void main(String[] args) {
        //System.out.println(maxProduct(new int[]{2,3,-2,4}));
        //System.out.println(maxProduct(new int[]{-2}));
        System.out.println(maxProduct(new int[]{-2,3,-4}));
    }

    public static int maxProduct(int[] nums) {
        int len = nums.length;
        int [] dp=new int[len+1];
        dp[0]=1;
        int ans=Integer.MIN_VALUE;
        for (int i = 1; i <= len; i++) {
            dp[i]=Math.max(dp[i-1]*nums[i-1],nums[i-1]);
            ans=Math.max(ans,dp[i]);
        }
        ans=Math.max(ans,dp[dp.length-1]);
        return ans;
    }
}
