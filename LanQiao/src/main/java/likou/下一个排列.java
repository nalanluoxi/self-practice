package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：下一个排列
 * @Date：2025/4/25 16:11
 * @Filename：下一个排列
 */
public class 下一个排列 {
    public static void main(String[] args) {
        int[] nums1 = {3,2,1};
        int[] nums = {1,5,1};
        nextPermutation(nums);
        for (int num : nums) {
            System.out.print(num+" ");

        }
    }

    public static void nextPermutation(int[] nums) {
        int i = nums.length-2;
        while (i>=0 && nums[i]>=nums[i+1]){
            i--;
        }
        if (i>=0){
            int j = nums.length - 1;
            while (j>=i && nums[j]<=nums[i]){
                j--;
            }
            swap(nums,i,j);
        }
        reverse(nums,i+1);
    }
    public static void reverse(int[] nums,int start){
        int left = start;
        int right = nums.length-1;
        while (left<right){
            swap(nums,left,right);
            left++;
            right--;
        }
    }

    public static void swap(int[] nums,int i,int j){
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

}
