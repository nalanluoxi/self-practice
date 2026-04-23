package likou;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：最大子数组和
 * @Date：2025/3/19 15:32
 * @Filename：最大子数组和
 */
public class 最大子数组和 {
    public static void main(String[] args) {
        int i = maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4});
        //int i = maxSubArray(new int[]{5,4,-1,7,8});
        //int i = maxSubArray(new int[]{-2,-1});
        System.out.println(i);
    }

    public static int maxSubArray(int[] nums) {
        int ans=Integer.MIN_VALUE;
        int sum=0;
        for (int i = 0; i < nums.length; i++) {
            sum+=nums[i];
            ans=Math.max(ans,sum);
            if (sum<0){
                sum=0;
            }
        }
        return ans==Integer.MIN_VALUE?0:ans;
    }

  /*  public static int maxSubArray(int[] nums) {
        int ans=Integer.MIN_VALUE;
        Deque<Integer> deque=new LinkedList<>();
        int[] sums=new int[nums.length];
        sums[0]=nums[0];
        for (int i = 1; i < nums.length; i++) {
            sums[i]=sums[i-1]+nums[i];
        }
        for (int i = 0; i < sums.length; i++) {
            System.out.println("i:"+i+" sum: "+sums[i]);
            while (!deque.isEmpty() && sums[i]<=sums[deque.peekLast()]){
                Integer last = deque.pollLast();
                int first = deque.isEmpty() ? 0 : sums[deque.peekFirst()];
                ans=Math.max(ans,sums[last]-first);
                ans=Math.max(ans,sums[last]);
            }
            deque.offerLast(i);
        }
        while (!deque.isEmpty()){
            Integer last = deque.pollLast();
            int first = deque.isEmpty() ? 0 : sums[deque.peekFirst()];
            ans=Math.max(ans,sums[last]-first);
            ans=Math.max(ans,sums[last]);
        }
        return ans==Integer.MIN_VALUE?0:ans;
    }*/
}
