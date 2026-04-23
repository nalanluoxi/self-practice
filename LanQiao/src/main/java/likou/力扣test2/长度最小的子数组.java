package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：长度最小的子数组
 * @Date：2025/5/18 10:37
 * @Filename：长度最小的子数组
 */
public class 长度最小的子数组 {
    public static void main(String[] args) {
        //int[] nums = {2, 3, 1, 2, 4, 3};
        int[] nums = {1, 2, 3, 4, 5};
        int target = 11;
        System.out.println(minSubArrayLen(target, nums));
    }

    public static int minSubArrayLen(int target, int[] nums) {
        int ans = Integer.MAX_VALUE;
        int left = 0, rigt = 0, sum = 0;
        while (rigt < nums.length) {
            sum += nums[rigt];
            while (sum >= target) {
                ans = Math.min(ans, rigt - left + 1);
                sum -= nums[left++];
            }
            rigt++;
        }
        return ans == Integer.MAX_VALUE ? 0 : ans;
    }
/*    public static void dps(int target, int[] nums, int index, List<Integer> tans){
        if (index==0){
            if (sum==target){
                ans=Math.min(ans,tans.size());
            }
            return;
        }
        if (sum==target){
            ans=Math.min(ans,tans.size());
            return;
        }else if (sum>target){
            return;
        }
        for (int i=index;i>=0;i--){
            sum+=nums[i];
            tans.add(nums[i]);
            dps(target,nums,i-1,tans);
            sum-=nums[i];
            tans.remove(tans.size()-1);
        }
    }*/
}
