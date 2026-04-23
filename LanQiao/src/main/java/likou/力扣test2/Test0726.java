package likou.力扣test2;

import 蓝桥杯真题.决赛13届.内存空间;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：Test0726
 * @Date：2025/7/26 16:18
 * @Filename：Test0726
 */
public class Test0726 {
    public static void main(String[] args) {
      //  System.out.println(maxSubArray(new int[]{-2,1,-3,4,-1,2,1,-5,4}));
    }



    public int maxProfit(int[] prices) {
        int len = prices.length;
        int[][]dp=new int[len][4];
        dp[0][0]=-prices[0];
        dp[0][2]=-prices[0];
        for (int i = 1; i < len; i++) {
            dp[i][0]=Math.max(dp[i-1][0],-prices[i]);
            dp[i][1]=Math.max(dp[i-1][1],dp[i-1][0]+prices[i]);
            dp[i][2]=Math.max(dp[i-1][2],dp[i-1][1]-prices[i]);
            dp[i][3]=Math.max(dp[i-1][3],dp[i-1][2]+prices[i]);
        }
        

        String string = "hello";
        string.equals("buhao");
        return dp[len-1][3];
    }


    public static int longestValidParentheses(String s) {
        int len = s.length();
        if (len==0){
            return 0;
        }
        int[]dp=new int[len];
        int ans=0;
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c=='('){
                dp[i]=0;
            } else if (c == ')') {
                if (i==0){
                    dp[i]=0;
                    continue;
                }
                int befor = dp[i - 1];
                if (i-befor-1<0||s.charAt(i-befor-1)!='('){
                    dp[i]=0;
                } else {
                    dp[i]=2+befor;
                    if (i-befor-2>=0){
                        dp[i]+=dp[i-befor-2];
                    }
                }

            }
            ans=Math.max(ans,dp[i]);
        }
        return ans;
    }

    public static int subarraySum(int[] nums, int k) {
        Map<Integer,Integer> map = new HashMap<>();
        map.put(0,1);
        int count=0;
        int pre=0;
        for (int num : nums) {
            pre+=num;
            if (map.containsKey(pre-k)){
                count+=map.get(pre-k);
            }
            map.put(pre,map.getOrDefault(pre, 0)+1);
        }
        return count;
    }



    public static int minDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();
        if (len1*len2==0){
            return len1+len2;
        }
        int[][] dp=new int[len1+1][len2+1];
        for (int i = 0; i <= len1; i++) {
            dp[i][0]=i;
        }
        for (int i = 0; i <= len2; i++) {
            dp[0][i]=i;
        }
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (word1.charAt(i-1)==word2.charAt(j-1)){
                    dp[i][j]=dp[i-1][j-1];
                }else {
                    dp[i][j]=Math.min(dp[i-1][j-1],Math.min(dp[i-1][j],dp[i][j-1]))+1;
                }
            }
        }
        return dp[len1][len2];
    }

}
