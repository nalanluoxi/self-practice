package likou;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：绝对值差不超过限制的最长连续子数组
 * @Date：2025/3/4 21:38
 * @Filename：绝对值差不超过限制的最长连续子数组
 */
public class 绝对值差不超过限制的最长连续子数组 {
    public static void main(String[] args) {
        int i = longestSubarray(new int[]{8, 2, 4, 7}, 4);
        System.out.println(i);
    }


    static Deque<Integer> queueMax;
    static Deque<Integer> queueMin;

    public static int longestSubarray(int[] nums, int limit) {
        queueMax = new LinkedList<>();
        queueMin = new LinkedList<>();
        int max = 0;

        for (int i = 0, j = 0; i < nums.length; i++) {
            while (j < nums.length && isOk(nums[j], limit,nums)) {
                add(j, nums);
                j++;
            }
            max = Math.max(max, j - i);
            remove(i);
        }
        return max;
    }

    public static boolean isOk(int num, int limit,int [] nums) {
        int max = queueMax.isEmpty() ? num : Math.max(nums[queueMax.peekFirst()], num);
        int min = queueMin.isEmpty() ? num : Math.min(nums[queueMin.peekFirst()], num);
        return max - min <= limit;
    }

    public static void add(int index, int[] nums) {
        int addnum = nums[index];
        while (!queueMax.isEmpty() && nums[queueMax.peekLast()] <= addnum) {
            queueMax.pollLast();
        }
        queueMax.offerLast(index);
        while (!queueMin.isEmpty() && nums[queueMin.peekLast()] >= addnum) {
            queueMin.pollLast();
        }
        queueMin.offerLast(index);
    }

    public static void remove(int index) {
        while (!queueMin.isEmpty() && queueMin.peekFirst() <= index) {
            queueMin.pollFirst();
        }
        while (!queueMax.isEmpty() && queueMax.peekFirst() <= index) {
            queueMax.pollFirst();
        }
    }


}
