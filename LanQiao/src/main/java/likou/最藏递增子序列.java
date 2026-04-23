package likou;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：最藏递增子序列
 * @Date：2025/3/31 11:55
 * @Filename：最藏递增子序列
 */
public class 最藏递增子序列 {
    public static void main(String[] args) {
       // int[] nums = {10, 9, 2, 5, 3, 7, 101, 18};
        int[] nums = {0,1,0,3,2,3};
        int i = lengthOfLIS(nums);
        System.out.println(i);

    }


    static int[] dp;
    static int[] nums;
    public static int lengthOfLIS(int[] num) {
        if (num==null||num.length==0){
            return 0;
        }
        if (num.length==1){
            return 1;
        }
        int ans=0;
        nums=num;
        dp=new int[nums.length];
        dp[0]=1;
        for (int i = 0; i < num.length; i++) {
            ans=Math.max(ans,dfs(i));
        }
        return ans;
    }
    public static int dfs(int index){
/*        if (index==0){
            return 1;
        }*/
        if (dp[index]!=0){
            return dp[index];
        }
        dp[index]=1;
        for (int i = 0; i <index; i++) {
            if (nums[index]>nums[i]){
                dp[index]=Math.max(dp[index],dfs(i)+1);
            }
        }
        return dp[index];
    }

  /*  public static int lengthOfLIS(int[] nums) {
        if (nums==null||nums.length==0){
            return 0;
        }
        if (nums.length==1){
            return 1;
        }
        int[] dp=new int[nums.length];
        int ans=Integer.MIN_VALUE;
        dp[0]=1;
        for (int i = 1; i < nums.length; i++) {
            dp[i]=1;
            for (int j = 0; j < i; j++) {
                if (nums[i]>nums[j]){
                    dp[i]=Math.max(dp[i],dp[j]+1);
                }
            }
            ans=Math.max(ans,dp[i]);
        }
        return ans;
    }*/

   /* static Deque<Integer> deque;
    public static int lengthOfLIS(int[] nums) {
        deque=new LinkedList<>();
        for (int i = 0; i < nums.length; i++) {
            while (!deque.isEmpty() && deque.peekLast()>=nums[i]){
                deque.pollLast();
            }
            deque.addLast(nums[i]);
        }
        //System.out.println(deque.size());
        return deque.size();
    }*/
}
