package likou.单调栈;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou.单调栈
 * @Project：LanQiaoBei
 * @name：和至少为k的子数组
 * @Date：2025/6/14 11:02
 * @Filename：和至少为k的子数组
 */
public class 和至少为k的子数组 {

    public static void main(String[] args) {
        int[] nums = {1};
        int k = 1;
        int i = shortestSubarray(nums, k);
        System.out.println(i);
    }
    public static int shortestSubarray(int[] nums, int k) {
        int len=nums.length;
        long[] presum=new long [len+1];
        presum[0]=0;
        for(int i=0;i<len;i++){
            presum[i+1]=presum[i]+nums[i];
        }
        Deque<Integer> deque=new LinkedList<>();
        int ans=Integer.MAX_VALUE;
        for(int i=0;i<len+1;i++){
            while(!deque.isEmpty()&& presum[i]-presum[deque.peekFirst()]>=k){
                ans=Math.min(ans,i-deque.pollFirst());
            }
            while(!deque.isEmpty() && presum[i] <= presum[deque.peekLast()]){
                deque.pollLast();
            }// 移除队尾元素，保持单调性
            deque.offerLast(i);
        }
        return ans==Integer.MAX_VALUE?-1:ans;
    }
}
