package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：长度最小的子数组
 * @Date：2025/1/22 12:52
 * @Filename：长度最小的子数组
 */
public class 长度最小的子数组 {
    public static void main(String[] args) {

        int i = minSubArrayLen(7, new int[]{2, 3, 1, 2, 4, 3});
        System.out.println(i);
    }

    public static int minSubArrayLen(int target, int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        int count = Integer.MAX_VALUE;
        int start = 0, end = 0, sum = 0;
        while (end < nums.length) {
            sum += nums[end];
            while (sum >= target) {
                count = Math.min(count, end - start + 1);
                sum -= nums[start];
                start++;
            }
            end++;
        }
        return count == Integer.MAX_VALUE ? 0 : count;
    }
}
