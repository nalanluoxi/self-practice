package likou;

import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：移除元素2
 * @Date：2025/2/12 21:41
 * @Filename：移除元素2
 */
public class 移除元素2 {
    public static void main(String[] args) {
        System.out.println(removeElement(new int[]{3, 2, 2, 3}, 3));
    }

    public static int removeElement(int[] nums, int val) {
        int fast=0;
        int slow=0;
        while (fast<nums.length){
            if (nums[fast]!=val){
                nums[slow]=nums[fast];
                slow++;
            }
            fast++;
        }
        return slow;
    }



}
