package likou.力扣test2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：删除有序数组中的重复项
 * @Date：2025/7/5 10:46
 * @Filename：删除有序数组中的重复项
 */
public class 删除有序数组中的重复项 {
    public static void main(String[] args) {
        int[]nums={1,1,2};
        System.out.println(removeDuplicates(nums));
        for (int num : nums) {
            System.out.println(num);
        }
    }
    public static int removeDuplicates1(int[] nums) {
        int n = nums.length;
        if (n == 0) {
            return 0;
        }
        int fast = 1, slow = 1;
        while (fast < n) {
            if (nums[fast] != nums[fast - 1]) {
                nums[slow] = nums[fast];
                ++slow;
            }
            ++fast;
        }
        return slow;
    }

    public static int removeDuplicates(int[] nums) {
        if (nums.length<=1){
            return nums.length;
        }
        int slow =1,fast=1;
        while (fast!=nums.length){
            if (nums[fast]!=nums[fast-1]){
                nums[slow++]=nums[fast++];
            }else{
                fast++;
            }
        }
        return slow;
    }
}
