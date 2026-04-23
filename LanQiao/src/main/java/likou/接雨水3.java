package likou;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：接雨水3
 * @Date：2025/3/31 19:23
 * @Filename：接雨水3
 */
public class 接雨水3 {
    public static void main(String[] args) {
        int[] height = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        System.out.println(trap(height));
    }

    static Deque<Integer> deque;

    public static int trap(int[] height) {
        deque = new LinkedList<>();
        deque.offerLast(0);
        int ans = 0;
        for (int i = 1; i < height.length; i++) {
            Integer p = deque.peekLast();
            if (height[i] < height[p]) {
                deque.offerLast(i);
            } else if (height[i] == height[p]) {
                deque.pollLast();
                deque.offerLast(i);
            } else if (height[i] > height[p]) {
                while (!deque.isEmpty() && height[deque.peekLast()] < height[i]) {
                    Integer last = deque.pollLast();
                    if (!deque.isEmpty()) {
                        Integer left = deque.peekLast();
                        int h = Math.min(height[left], height[i]) - height[last];
                        int w = i - left - 1;
                        if (h * w > 0) {
                            ans += h * w;
                        }
                    }
                }
                deque.offerLast(i);
            }
        }
        return ans;
    }
}
