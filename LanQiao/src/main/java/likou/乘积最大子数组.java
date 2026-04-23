package likou;

import java.util.List;
import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：乘积最大子数组
 * @Date：2025/5/12 10:37
 * @Filename：乘积最大子数组
 */
public class 乘积最大子数组 {
    public static void main(String[] args) {
        System.out.println(maxProduct(new int[]{2,3,-2,4}));
        System.out.println(maxProduct(new int[]{-2,0,-1}));
    }


    static int ans;
    static int[]dpmax;
    static int[]dpmin;
    public static int maxProduct(int[] nums) {
        if (nums.length==0){
            return 0;
        }
        if (nums.length==1){
            return nums[0];
        }
        ans=0;
        dpmax=new int[nums.length];
        dpmin=new int[nums.length];
        dpmax[0]=nums[0];
        dpmin[0]=nums[0];
        for (int i = 1; i < nums.length; i++) {
            dpmax[i]=Math.max(nums[i],Math.max(nums[i]*dpmax[i-1],nums[i]*dpmin[i-1]));
            dpmin[i]=Math.min(nums[i],Math.min(nums[i]*dpmax[i-1],nums[i]*dpmin[i-1]));
            //ans=Math.max(ans,dpmax[i]);
        }
        for (int i = 0; i < nums.length; i++) {
            ans=Math.max(ans,dpmax[i]);
        }
        return ans;
    }
}
