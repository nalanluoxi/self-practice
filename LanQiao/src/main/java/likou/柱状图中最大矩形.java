package likou;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：柱状图中最大矩形
 * @Date：2025/3/10 21:08
 * @Filename：柱状图中最大矩形
 */
public class 柱状图中最大矩形 {
    public static void main(String[] args) {
        int[] height = {2, 1, 5, 6, 2, 3};
        System.out.println(largestRectangleArea(height));
    }

    static Deque<Integer> deque;

    public static int largestRectangleArea(int[] nums) {
        int ans = Integer.MIN_VALUE;
        deque = new LinkedList<>();
        for (int i = 0; i < nums.length; i++) {
            while (!deque.isEmpty() && nums[i] <= nums[deque.peekLast()]) {
                Integer mid = deque.pollLast();
                int left=deque.isEmpty()?-1:deque.peekLast();
                ans = Math.max(ans, nums[mid] * (i - left - 1));
            }
            deque.offerLast(i);
        }
        while (!deque.isEmpty()) {
            Integer mid = deque.pollLast();
            int left=deque.isEmpty()?-1:deque.peekLast();
            ans = Math.max(ans, nums[mid] * (nums.length - left - 1));
        }
        return ans;
    }


}
