package likou.贪心;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：likou.贪心
 * @Project：LanQiaoBei
 * @name：最大子数组和
 * @Date：2025/3/12 10:45
 * @Filename：最大子数组和
 */
public class 最大子数组和 {
    public static void main(String[] args) {
        int[] nums = {-2,1,-3,4,-1,2,1,-5,4};
        //int[] nums = {1,2,3,4,5,6,-1,3,-2,1,-99,999999};
        //int[] nums = {1};
        //int[] nums = {-2,-1};
        System.out.println(maxSubArray(nums));
    }

    /*public static int maxSubArray(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }

        int res = nums[0];
        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            dp[i] = Math.max(dp[i - 1] + nums[i], nums[i]);
            res = res > dp[i] ? res : dp[i];
        }
        return res;
    }*/

    public static int maxSubArray(int[] nums) {
        int [] dp=new int[nums.length];
        dp[0]=Math.max(0,nums[0]);
        int max=Integer.MIN_VALUE;
        for (int i = 1; i < nums.length; i++) {
            dp[i]=Math.max(dp[i-1]+nums[i],nums[i]);
            max=Math.max(max,dp[i]);
        }
        return max;
    }



  /*  public static int maxSubArray(int[] nums) {
        if (nums.length == 1){
            return nums[0];
        }
        int sum = Integer.MIN_VALUE;
        int count = 0;
        for (int i = 0; i < nums.length; i++){
            count += nums[i];
            sum = Math.max(sum, count); // 取区间累计的最大值（相当于不断确定最大子序终止位置）
            if (count <= 0){
                count = 0; // 相当于重置最大子序起始位置，因为遇到负数一定是拉低总和
            }
        }
        return sum;
    }*/
  /*  public static int maxSubArray(int[] nums) {
        if (nums.length == 1){
            return nums[0];
        }
        int sum = Integer.MIN_VALUE;
        int count = 0;
        for (int i = 0; i < nums.length; i++){
            count += nums[i];
            sum = Math.max(sum, count); // 取区间累计的最大值（相当于不断确定最大子序终止位置）
            if (count <= 0){
                count = 0; // 相当于重置最大子序起始位置，因为遇到负数一定是拉低总和
            }
        }
        return sum;
    }
*/

    /**
     * -2, 1,-3,4,-1,2,1,-5,4
     *
     * -2 -1 -4 0 -1 1 2 -3 1
     *
     *
     * @param nums
     * @return
     */
  /*  public static int maxSubArray(int[] nums) {
        if (nums.length==1){
            return nums[0];
        }
        int max=nums[0];
        int [] sum=new int[nums.length];
        //sum[0]=0;
        sum[0]=nums[0];
        for (int i = 1; i < nums.length; i++) {
            sum[i]=nums[i]+sum[i-1];
        }
        Deque<Integer> deque=new LinkedList<>();
        for (int i = 0; i < sum.length; i++) {
            while (!deque.isEmpty() && sum[i] < sum[deque.peekLast()]){
                Integer cur = deque.pollLast();
                int left=-1;
                if (deque.isEmpty()){
                    if (cur-1>=0){
                        left=cur-1;
                    }
                }else {
                    Integer first = deque.peekFirst();
                    if (first-1>=0){
                        left=first-1;
                    }
                }
                if (left==-1){
                    max=Math.max(max,sum[cur]);
                }else {
                    max=Math.max(max,sum[cur]-sum[left]);
                }
            }
            deque.addLast(i);
        }
        while (!deque.isEmpty()){
            Integer cur = deque.pollLast();
            int left=-1;
            if (deque.isEmpty()){
                if (cur-1>=0){
                    left=cur-1;
                }
            }else {
                Integer first = deque.peekFirst();
                if (first-1>=0){
                    left=first-1;
                }
            }
            if (left==-1){
                max=Math.max(max,sum[cur]);
            }else {
                max=Math.max(max,sum[cur]-sum[left]);
            }
        }
        return max;
    }*/
}
