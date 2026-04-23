package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：打家劫舍
 * @Date：2025/2/9 18:33
 * @Filename：打家劫舍
 */
public class 打家劫舍 {
    public static void main(String[] args) {

        //System.out.println(rob(new int[]{2,1,1,2}));
        System.out.println(rob(new int[]{2,7,9,3,1}));
    }

    public static int rob(int[] nums) {
        if (nums.length==1){
            return nums[0];
        }
        int len = nums.length;
        int []dp=new int[len+1];
        dp[len-1]=nums[len-1];
        dp[len-2]=nums[len-2];
        for (int i=len-3;i>=0;i--){
            dp[i]=nums[i]+Math.max(dp[i+2],dp[i+3]);
            //System.out.println("dp["+i+"] = " + dp[i]+"dp["+(i+2)+"] = " + dp[i+2]+"dp["+(i+3)+"] = " + dp[i+3]);
           // System.out.println();
        }
        return Math.max(dp[0],dp[1]);
    }
}
