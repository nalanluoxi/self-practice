package luogu;

import java.util.PriorityQueue;

/**
 * @Author 纳兰洛熙
 * @Package：luogu
 * @Project：LanQiaoBei
 * @name：数组中第K哥最大元素
 * @Date：2025/3/19 10:04
 * @Filename：数组中第K哥最大元素
 */
public class 数组中第K哥最大元素 {
    public static void main(String[] args) {
        int[] nums = {3,2,1,5,6,4};
        int k = 2;
        int i = findKthLargest(nums, k);
        System.out.println(i);
    }

    public static int findKthLargest(int[] nums, int k) {
        int ans=0;
        PriorityQueue<Integer> queue=new PriorityQueue<>((a,b)->b-a);
        for (int i = 0; i < nums.length; i++) {
            queue.offer(nums[i]);
        }
        for (int i = 0; i < k-1; i++) {
           queue.poll();
        }
        ans=queue.poll();
        return ans;
    }
}
