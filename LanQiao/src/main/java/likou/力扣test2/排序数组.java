package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：排序数组
 * @Date：2025/6/6 11:04
 * @Filename：排序数组
 */
public class 排序数组 {

    public static void main(String[] args) {
        //int[] array = sortArray(new int[]{5, 2, 3, 1});
        int[] array = sortArray(new int[]{-2, 3, -5});
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ,");
        }
    }

    public static int[] sortArray(int[] nums) {
        return quick(nums);
    }

    public static int[] quick(int[] nums) {

        for (int i = 1; i < nums.length; i++) {
            int base = nums[i];
            int j = i - 1;
            while (j >= 0 && base< nums[j]) {
                nums[j + 1] = nums[j];
                j--;
            }
            nums[j + 1] = base;
        }
        return nums;
    }
/*    public static int[] sortArray(int[] nums) {
        sort(nums, 0, nums.length - 1);
        return nums;
    }

    public static void sort(int[] nums, int left, int right) {
        if (left >= right) {
            return;
        }
        int mid = left + (right - left) / 2;
        sort(nums, left, mid);
        sort(nums, mid + 1, right);
        addTwo(nums, left, mid, right);
    }

    public static void addTwo(int[] nums, int letf, int mid, int right) {
        int i = letf, j = mid + 1, l = 0;
        int[] temp = new int[right - letf + 1];
        while (i <= mid && j <= right) {
            if (nums[i] <= nums[j]) {
                temp[l++] = nums[i++];
            } else if (nums[j] < nums[i]) {
                temp[l++] = nums[j++];
            }
        }
        while (i <= mid) {
            temp[l++] = nums[i++];
        }
        while (j <= right) {
            temp[l++] = nums[j++];
        }
        for (int k = 0; k < temp.length; k++) {
            nums[letf + k] = temp[k];
        }
    }*/
}
