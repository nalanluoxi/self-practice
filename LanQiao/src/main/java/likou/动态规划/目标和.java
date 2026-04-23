package likou.动态规划;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：目标和
 * @Date：2025/6/24 17:00
 * @Filename：目标和
 */
public class 目标和 {


    public static void main(String[] args) {
        int[] nums = {0,1};
        int target = 1;
        System.out.println(findTargetSumWays(nums, target));
    }

    public static int findTargetSumWays(int[] nums, int target) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        // 如果target超出了可能的范围，直接返回0
        if (Math.abs(target) > sum) {
            return 0;
        }
        // dp数组大小应该是nums.length * (2*sum + 1)
        int[][] dp = new int[nums.length][2 * sum + 1];
        for (int[] row : dp) {
            Arrays.fill(row, -1);
        }

        return dfs(nums, target, 0, 0, dp, sum);
    }

    public static int dfs(int[] nums, int target, int index, int curSum, int[][] dp, int total) {
        if (index == nums.length) {
            return curSum == target ? 1 : 0;
        }

        // 将curSum映射到dp数组下标：curSum的范围是[-total, total]，加上total将范围映射到[0, 2*total]
        if (dp[index][curSum + total] != -1) {
            return dp[index][curSum + total];
        }

        int add = dfs(nums, target, index + 1, curSum + nums[index], dp, total);
        int subtract = dfs(nums, target, index + 1, curSum - nums[index], dp, total);

        dp[index][curSum + total] = add + subtract;
        return dp[index][curSum + total];
    }

    /*    public static int findTargetSumWays(int[] nums, int target) {
            HashMap<Integer,HashMap<Integer,Integer>>dp=new HashMap<>();
            return dfs(nums,target,0,0,dp);
        }*/

  /*  public static int dfs(int[]nums ,int tar,int i,int sum,HashMap<Integer,HashMap<Integer,Integer>>dp){
        if (i==nums.length){
            return sum==tar?1:0;
        }else {
            if (dp.containsKey(i)&&dp.get(i).containsKey(sum)){
                return dp.get(i).get(sum);
            }
            int t=dfs(nums,tar,i+1,sum+nums[i],dp)+dfs(nums,tar,i+1,sum-nums[i],dp);
            if (!dp.containsKey(i)){
                dp.put(i,new HashMap<>());
            }
            dp.get(i).put(sum,t);
            return t;
        }
    }*/


   /* static int []arr;
    static int target;
    static List<Integer>list;
    public static int findTargetSumWays(int[] nums, int targets) {
        arr = nums;
        target=targets;
        ans=0;
        list=new ArrayList<>();
        dfs(0,0);
        return ans;
    }

    static  int ans;
    public static void dfs(int start,int sum){
        if (list.size()==arr.length){
            if (sum==target){
                ans++;
                System.out.println(list.toString());
            }
            return;
        }
        for (int i = start; i < arr.length; i++) {
            list.add(arr[i]);
            dfs(i+1,sum+arr[i]);
            list.remove(list.size()-1);
            list.add(-arr[i]);
            dfs(i+1,sum-arr[i]);
            list.remove(list.size()-1);
        }
    }*/
}
