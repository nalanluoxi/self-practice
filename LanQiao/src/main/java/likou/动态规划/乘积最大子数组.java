package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：乘积最大子数组
 * @Date：2025/6/29 22:22
 * @Filename：乘积最大子数组
 */
public class 乘积最大子数组 {
    public static void main(String[] args) {
        int[]nums={2,3,-2,4};
        System.out.println(maxProduct(nums));
    }


    public static int maxProduct(int[] nums) {
        int len = nums.length;
        int []maxdp=new int[len];
        int []mindp=new int[len];
        maxdp[0]=nums[0];
        mindp[0]=nums[0];
        int ans=nums[0];
        for (int i = 1; i < len; i++) {
            maxdp[i]=Math.max(maxdp[i-1]*nums[i],Math.max(nums[i],nums[i]*mindp[i-1]));
            mindp[i]=Math.min(mindp[i-1]*nums[i],Math.min(nums[i],nums[i]*maxdp[i-1]));
            ans=Math.max(ans,maxdp[i]);
        }
        return ans;
    }
}
