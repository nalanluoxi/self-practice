package likou;

public class 移动0 {

    public static void main(String[] args) {
        int[] num={0,1,3,0,4};
        moveZeroes(num);
    }


        public static void moveZeroes(int[] nums) {
            int n = nums.length, left = 0, right = 0;
            while (right < n) {
                if (nums[right] != 0) {
                    swap(nums, left, right);
                    left++;
                }
                right++;
            }
        }

        public static void swap(int[] nums, int left, int right) {
            int temp = nums[left];
            nums[left] = nums[right];
            nums[right] = temp;
        }



}
