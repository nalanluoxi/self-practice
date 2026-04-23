package likou.力扣test2;

public class Test0124 {
    public static void main(String[] args) {
       /* int[]nums={-2,1,-3,4,-1,2,1,-5,4};
        System.out.println(maxSubArray(nums));*/
        System.out.println(longestPalindrome("babad"));
    }


    public static String longestPalindrome(String s) {
        int start=0;
        int maxlen=1;
        int len = s.length();
        boolean[][]dp=new boolean[len][len];
        for (int i = 0; i < len; i++) {
            dp[i][i]=true;
        }
        for (int l = 1; l <= len; l++) {
            for (int i = 0; i <= len; i++) {
                int j = i + l;
                if (j>=len){
                    continue;
                }
                if (s.charAt(i)==s.charAt(j)){
                    if (l<=2){
                        dp[i][j]=true;
                    }else {
                        dp[i][j]=dp[i+1][j-1];
                    }
                }else {
                    dp[i][j]=false;
                }

                if (dp[i][j] && l>=maxlen){
                    start=i;
                    maxlen=l+1;
                }

            }
        }

        return s.substring(start,start+maxlen);
    }


    public static int maxSubArray(int[] nums) {
        int sum=nums[0];
        int ans=sum;

        for (int i = 1; i < nums.length; i++) {
            sum=Math.max(nums[i],sum+nums[i]);
            ans=Math.max(ans,sum);
        }
        return ans;

    }

    public static int maxSubArray1(int[] nums) {
        int len = nums.length;
        if (len==0){
            return 0;
        } else if (len==1) {
            return nums[0];
        }
        int ans=nums[0];

        int[]dp=new int[len];
        dp[0]=nums[0];
        for (int i = 1; i < len; i++) {
            dp[i]=Math.max(nums[i],dp[i-1]+nums[i]);
            ans=Math.max(ans,dp[i]);
        }
        return  ans;
    }
}
