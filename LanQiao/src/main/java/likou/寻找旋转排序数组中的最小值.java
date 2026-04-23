package likou;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：寻找旋转排序数组中的最小值
 * @Date：2025/6/4 10:23
 * @Filename：寻找旋转排序数组中的最小值
 */
public class 寻找旋转排序数组中的最小值 {
    public static void main(String[] args) {

    }

/*    public static int findMin(int[] nums) {
        Arrays.sort(nums);
        return nums[0];
    }*/

    public static int findMin(int[] nums) {
        int l=0;
        int r=nums.length-1;
        if (nums[r]>nums[l]){
            return nums[l];
        }
        while (l<r){
            int mid = l + (r - l) / 2;
            if (nums[mid]<nums[r]){
                r=mid;
            }else {
                l=mid+1;
            }
        }
        return nums[l];
    }

  /*  public static int erfen(int[] nums) {
        int l=0,r=nums.length-1;
        int mid=0;
        while (l<r){
            mid=l+(r-l)/2;
            if (nums[mid])
        }
    }*/
}
