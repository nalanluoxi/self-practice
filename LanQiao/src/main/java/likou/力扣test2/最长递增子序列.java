package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：最长递增子序列
 * @Date：2025/6/7 21:21
 * @Filename：最长递增子序列
 */
public class 最长递增子序列 {


    public static void main(String[] args) {
        int[] nums={10,9,2,5,3,7,101,18};
        System.out.println(lengthOfLIS(nums));
    }
    public static int lengthOfLIS(int[] nums) {
        int len=nums.length;
        int []dp=new int[len+1];
        dp[len-1]=1;
        int ans=1;
        for(int i=len-2;i>=0;i--){
            dp[i]=1;
            for(int j=i+1;j<len;j++){
                if(nums[i]<nums[j]){
                    dp[i]=Math.max(dp[j]+1,dp[i]);
                }
            }
            ans=Math.max(dp[i],ans);
        }
        return ans;
    }
}
