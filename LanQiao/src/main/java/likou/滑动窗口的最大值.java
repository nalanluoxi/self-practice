package likou;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：滑动窗口的最大值
 * @Date：2025/3/3 21:21
 * @Filename：滑动窗口的最大值
 */
public class 滑动窗口的最大值 {
    public static void main(String[] args) {
        int[] ints = maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
   /*     Queue<Integer> queue = new ArrayDeque<>();
        queue.add(1);
        queue.add(2);
        queue.add(3);
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());*/

    }


    static Deque<Integer> queue;

    public static int[] maxSlidingWindow(int[] nums, int k) {
       int [] ans=new int[nums.length-k+1];
       queue=new LinkedList<>();
       for (int i = 0; i < k; i++) {
           while (!queue.isEmpty() && nums[i] >= nums[queue.peekLast()]) {
               queue.pollLast();
           }
           queue.offerLast(i);
       }
       ans[0]=nums[queue.peekFirst()];
       for (int i = k; i < nums.length; i++) {
          while (!queue.isEmpty() && nums[i] >= nums[queue.peekLast()]) {
               queue.pollLast();
           }
           queue.offerLast(i);
           while (queue.peekFirst()<=i-k){
               queue.pollFirst();
           }
           ans[i-k+1]=nums[queue.peekFirst()];
       }
       return ans;
    }
}
