package likou;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：下一个更大元素2
 * @Date：2025/3/10 18:08
 * @Filename：下一个更大元素2
 */
public class 下一个更大元素2 {
    public static void main(String[] args) {
        int[] ints = nextGreaterElements(new int[]{1,2,3,2,1});
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }

    static int[] ans;
    static Deque<Integer> deque;

    public static int []nextGreaterElements(int[] nums){
        int [] nnums=new int[nums.length*2];
        for (int i = 0; i < nums.length; i++) {
            nnums[i]=nums[i];
            nnums[i+nums.length]=nums[i];
        }
        int[] help = help(nnums);
        int[] ans=new int[nums.length];
        for (int i = 0; i < ans.length; i++) {
            ans[i]=help[i];
        }
        return ans;
    }

    public static int[] help(int[] nums) {
        int len = nums.length;
        ans = new int[len];
        deque = new LinkedList<>();
        Arrays.fill(ans, -1);
        for (int i = 0; i < len; i++) {
            while (!deque.isEmpty() && nums[i] > nums[deque.peekLast()]) {
                ans[deque.pollLast()] = nums[i];
            }
            deque.offerLast(i);
        }
        while (!deque.isEmpty()) {
            int index = deque.pollLast();
            if (!deque.isEmpty()&&nums[index] < nums[deque.peekFirst()]) {
                ans[index] = nums[deque.peekFirst()];
            }
        }
        return ans;
    }
}
