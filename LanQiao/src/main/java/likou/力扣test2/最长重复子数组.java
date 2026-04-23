package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：最长重复子数组
 * @Date：2025/6/8 20:26
 * @Filename：最长重复子数组
 */
public class 最长重复子数组 {

    public static void main(String[] args) {
        //int length1 = findLength(new int[]{1, 2, 3, 2, 1}, new int[]{3, 2, 1, 4, 7});
        int length = findLength(new int[]{0,1,1,1,1}, new int[]{1,0,1,0,1});
        System.out.println(length);
    }


    public static int findLength(int[] nums1, int[] nums2) {
        int len1=nums1.length;
        int len2=nums2.length;
        int[][]dp=new int[len1+1][len2+1];
        int ans=0;
        for (int i = 1; i <= nums1.length; i++) {
            for (int j = 1; j <= nums2.length; j++) {
                if(nums1[i-1]==nums2[j-1]){
                    dp[i][j]=dp[i-1][j-1]+1;
                    ans=Math.max(dp[i][j],ans);
                }
            }
        }
        return ans;
    }

   /* public static int dfs(int i,int j){
        if (i<0||j<0){
            return 0;
        }
        if (dp[i][j]!=0){
            return dp[i][j];
        }
        if (arr1[i]==arr2[j]){
            dp[i][j]=dfs(i-1,j-1)+1;
        }else{
            dp[i][j]=Math.max(dfs(i-1,j),dfs(i,j-1));
        }
        return dp[i][j];
    }*/
}
