package likou;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：和至少为k的最短子数组
 * @Date：2025/3/7 11:18
 * @Filename：和至少为k的最短子数组
 */
public class 和至少为k的最短子数组 {
    public static void main(String[] args) {
        int i = shortestSubarray(new int[]{84, -37, 32, 40, 95}, 167);
        System.out.println(i);

    }

    static Deque<Integer> queue;
    static long[] sums;

    public static int shortestSubarray(int[] nums, int k) {
        sums = new long[nums.length + 1];
        sums[0] = 0;
        for (int i = 0; i < nums.length; i++) {
            sums[i + 1] = sums[i] + nums[i];
        }
        queue = new LinkedList<>();
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < sums.length; i++) {
            while (!queue.isEmpty() && sums[i] - sums[queue.peekFirst()] >= k) {
                min = Math.min(min, i - queue.pollFirst());
            }
            while (!queue.isEmpty() && sums[i] <= sums[queue.peekLast()]) {
                queue.pollLast();
            }
            queue.addLast(i);
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }
}
