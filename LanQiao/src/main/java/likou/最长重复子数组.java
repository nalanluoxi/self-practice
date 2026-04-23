package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：最长重复子数组
 * @Date：2025/5/17 23:17
 * @Filename：最长重复子数组
 */
public class 最长重复子数组 {
    public static void main(String[] args) {
        //int[] nums1 = {1, 2, 3, 2, 1};
        int[] nums1 = {0, 1, 1, 1, 1};
        //int[] nums2 = {3, 2, 1, 4, 7};
        int[] nums2 = {1, 0, 1, 0, 1};
        System.out.println(findLength(nums1, nums2));
    }

    static int ans;

    public static int findLength(int[] nums1, int[] nums2) {
        if (nums1.length == 0 || nums2.length == 0) {
            return 0;
        }
        ans = 0;
        int l1 = nums1.length;
        int l2 = nums2.length;
        int[][] dp = new int[l1 + 1][l2 + 1];
        for (int i = l1 - 1; i >= 0; i--) {
            for (int j = l2 - 1; j >= 0; j--) {
                if (nums1[i] == nums2[j]) {
                    dp[i][j] = dp[i + 1][j + 1] + 1;
                } else {
                    dp[i][j] = 0;
                }
                ans = Math.max(ans, dp[i][j]);
            }
        }
        return ans;
    }


}
