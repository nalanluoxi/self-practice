package luogu;

public class 练习 {



        // 主函数，用于测试
        public static void main(String[] args) {
            int[] nums = {1, 2, 3};
            permute(nums, 0, nums.length - 1);
        }

        // 用于全排列的递归函数
        public static void permute(int[] nums, int start, int end) {
            if (start == end) {
                // 输出一种排列
                printArray(nums);
            } else {
                for (int i = start; i <= end; i++) {
                    // 交换
                    swap(nums, start, i);
                    // 递归地排列剩余的元素
                    permute(nums, start + 1, end);
                    // 回溯，恢复数组原状以便下一次循环
                    swap(nums, start, i);
                }
            }
        }

        // 交换数组中两个位置的元素
        public static void swap(int[] nums, int i, int j) {
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }

        // 打印数组
        public static void printArray(int[] nums) {
            for (int num : nums) {
                System.out.print(num + " ");
            }
            System.out.println();
        }

}
