package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：轮转数组
 * @Date：2025/1/22 10:29
 * @Filename：轮转数组
 */
public class 轮转数组 {
    public static void main(String[] args) {
      //int [] nums= {1,2};
         int[] nums = {-1,-100,3,99};
        rotate(nums, 2);
        for (int num : nums) {
            System.out.print(num + " ");
        }

    }

    public static void rotate(int[] nums, int k) {
        if (nums.length == 1) {
            return;
        }
        if (k>nums.length){
            k=k%nums.length;
        }
        int [] temp=new int[nums.length];

        for (int i = nums.length-k, j=0; i < nums.length; i++,j++) {
            temp[j]=nums[i];
        }

        for (int i = 0,j=k; i < nums.length-k; i++,j++) {
            temp[j]=nums[i];
        }
        for (int i = 0; i < nums.length; i++) {
            nums[i]=temp[i];
        }
    }
}
