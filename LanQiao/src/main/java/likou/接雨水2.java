package likou;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：接雨水2
 * @Date：2025/3/10 18:31
 * @Filename：接雨水2
 */
public class 接雨水2 {
    public static void main(String[] args) {
        int[] height = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        System.out.println(trap(height));

    }


    //递减的栈
    static Deque<Integer> deque;

    public static int trap(int[] height) {
        deque = new LinkedList<>();
        int ans = 0;
        deque.offerLast(0);
        for (int i = 1; i < height.length; i++) {
            Integer p = deque.peekLast();
            if (deque.isEmpty() || height[i] < height[p]) {
                deque.offerLast(i);
            } else if (height[i] == height[p]) {
                deque.pollLast();
                deque.offerLast(i);
            } else {
                while (!deque.isEmpty() && height[p] < height[i]) {
                    Integer mid = deque.pollLast();
                    if (!deque.isEmpty()) {
                        Integer left = deque.peekLast();
                        int h = Math.min(height[left], height[i]) - height[mid];
                        int w = i - left - 1;
                        if (h * w > 0) {
                            ans += h * w;
                        }
                        p=deque.peekLast();
                    }
                }
                deque.offerLast(i);
            }
        }
        return ans;
    }
}
