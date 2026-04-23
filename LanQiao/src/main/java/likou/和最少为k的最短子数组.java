package likou;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：和最少为k的最短子数组
 * @Date：2025/3/4 19:53
 * @Filename：和最少为k的最短子数组
 */
public class 和最少为k的最短子数组 {
    public static void main(String[] args) {

        int i = shortestSubarray(new int[]{1}, 1);
        System.out.println(i);
    }


    static Deque<Integer> queue;

    static long[] ands;

    public static int shortestSubarray(int[] nums, int k) {
   /*     if (nums.length == 0) {
            return -1;
        }
        if (nums.length == 1) {
            return nums[0] >= k ? 1 : -1;
        }*/
        ands = new long[nums.length + 1];
        ands[0] = 0;
        int len = nums.length;
        for (int i = 0; i < len; i++) {
            ands[i + 1] = ands[i] + nums[i];
        }
        int min = Integer.MAX_VALUE;
        queue = new LinkedList<>();
        for (int i = 0; i <= len; i++) {
            while (!queue.isEmpty() && ands[i] - ands[queue.peekFirst()] >= k) {
                min = Math.min(min, i - queue.pollFirst());
            }
            while (!queue.isEmpty() && ands[i] <= ands[queue.peekLast()]) {
                queue.pollLast();
            }
            queue.offerLast(i);
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }
}
