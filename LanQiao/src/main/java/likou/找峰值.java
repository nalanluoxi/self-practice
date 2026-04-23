package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：找峰值
 * @Date：2025/1/19 9:04
 * @Filename：找峰值
 */
public class 找峰值 {
    public static void main(String[] args) {
        int peakElement = findPeakElement(new int[]{1,2,3,4,3,2});
        System.out.println(peakElement);
    }

    public static int findPeakElement(int[] nums) {
        int len = nums.length;
        if (len == 0) {
            return -1;
        }
        if (len == 1) {
            return 0;
        }
        if (nums[0] > nums[1]) {
            return 0;
        }
        if (nums[len - 1] > nums[len - 2]) {
            return len - 1;
        }
        int l = 0, r = len - 1;
        while (l <= r) {
            int m = (l +r) / 2;
            if (m == 0) {
                m=1;
            }
            if (nums[m] > nums[m - 1] && nums[m] > nums[m + 1]) {
                return m;
            }
            if (nums[m] < nums[m - 1]) {
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return -1;
    }
}
