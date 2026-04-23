package likou.动态规划;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：最长公共子数组
 * @Date：2025/6/29 22:27
 * @Filename：最长公共子数组
 */
public class 最长公共子数组 {
    public static void main(String[] args) {
        int[]nums1={1,2,3,2,1};
        int[]nums2={3,2,1,4,7};
        System.out.println(findLength(nums1,nums2));
    }

    public static int findLength(int[] nums1, int[] nums2) {
        int n = nums1.length;
        int m = nums2.length;
        if (n*m==0){
            return 0;
        }
        int[][] dp = new int[n + 1][m + 1];
        int ans=0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (nums1[i-1]==nums2[j-1]){
                    dp[i][j]=dp[i-1][j-1]+1;
                }
                ans=Math.max(ans,dp[i][j]);
            }
        }
        return ans;
    }
}
