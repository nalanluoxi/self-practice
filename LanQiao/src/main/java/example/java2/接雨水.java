package example.java2;

import javax.print.attribute.standard.PrinterMakeAndModel;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2
 * @Project：LanQiaoBei
 * @name：接雨水
 * @Date：2025/4/18 11:35
 * @Filename：接雨水
 */
public class 接雨水 {
    public static void main(String[] args) {
        System.out.println(trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}));
    }

    public static int trap(int[] nums) {
        int ans = 0;
        Deque<Integer> deque=new LinkedList<>();
        deque.offerLast(0);
        for (int i = 1; i < nums.length; i++) {
            Integer last = deque.peekLast();
            if (deque.isEmpty()||nums[i]<nums[last]){
                deque.offerLast(i);
            } else if (nums[i]==nums[last]) {
                deque.pollLast();
                deque.offerLast(i);
            } else if (nums[i] > nums[last]) {
                while (!deque.isEmpty() && nums[i] > nums[last]) {
                    Integer mid = deque.pollLast();
                    if (!deque.isEmpty()){
                        Integer left = deque.peekLast();
                        int h=Math.min(nums[left],nums[i])-nums[mid];
                        int w=i-left-1;
                        ans+=h*w;
                        last=deque.peekLast();
                    }
                }
                deque.offerLast(i);
            }
        }
        return ans;
    }
}
