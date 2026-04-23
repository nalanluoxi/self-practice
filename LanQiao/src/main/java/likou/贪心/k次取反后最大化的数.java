package likou.贪心;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou.贪心
 * @Project：LanQiaoBei
 * @name：k次取反后最大化的数
 * @Date：2025/3/13 17:32
 * @Filename：k次取反后最大化的数
 */
public class k次取反后最大化的数 {
    public static void main(String[] args) {
        //System.out.println(largestSumAfterKNegations(new int[]{2,-3,-1,5,-4},2));
        System.out.println(largestSumAfterKNegations(new int[]{-2,5,0,2,-2},3));
    }

    public static int largestSumAfterKNegations(int[] nums, int k) {
        Arrays.sort(nums);
        int sum=0;
        for (int i = 0; i < nums.length; i++) {
            int now = nums[i];
            if (now<0){
                if (k>0){
                    k--;
                    now=-now;
                }
            }
            nums[i]=now;
        }
        Arrays.sort(nums);
        while (k>0){
            nums[0]=-nums[0];
            k--;
        }
        for (int i = 0; i < nums.length; i++) {
            sum+=nums[i];
        }

        return sum;
    }
}
