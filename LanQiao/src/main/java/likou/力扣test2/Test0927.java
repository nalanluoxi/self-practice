package likou.力扣test2;

import javax.naming.ldap.LdapContext;
import java.awt.image.BandedSampleModel;
import java.lang.reflect.Array;
import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：Test0927
 * @Date：2025/9/27 16:15
 * @Filename：Test0927
 */
public class Test0927 {
    public static void main(String[] args) {


        /*List<String> leet = List.of("leet", "code");
        boolean b = wordBreak("leetcode", leet);
        System.out.println(b);*/
        /*System.out.println(lengthOfLIS(new int[]{10,9,2,5,3,7,101,18}));*/

        //System.out.println(canPartition(new int[]{1,2,5}));


        /*System.out.println(longestValidParentheses("()()))))()()("));*/


        /*int[] ints = topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2);
        for (int i = 0; i < ints.length; i++) {
            System.out.println(ints[i]);
        }*/
        /*System.out.println(jump(new int[]{2,3,0,1,4}));*/
        System.out.println(maxProfit(new int[]{7,1,5,3,6,4}));
    }

    public static int maxProfit(int[] prices) {
        int ans=0;
        int max=prices[prices.length-1];

        for (int i = prices.length-1; i >=0; i--) {
            int t = max - prices[i];
            ans=Math.max(ans,t);
            max=Math.max(max,prices[i]);
        }
        return ans;
    }

    public static boolean canJump(int[] nums) {
        int tmax=nums[0];
        for (int i = 0; i <= tmax; i++) {
            if (i+nums[i]>= nums.length-1){
                return true;
            }
            tmax=Math.max(tmax,nums[i]+i);
        }
        return false;
    }
    public static int jump(int[] nums) {
        if (nums.length<=1){
            return 0;
        }
        int max=nums[0];
        int num=0;
        int ans=1;
        for (int i = 1; i <= max; i++) {
            num=Math.max(num,i+nums[i]);
            if (i==max){
                ans++;
                max=Math.max(max,nums[i]);
                if (max>nums.length-1){
                    return ans;
                }
            }
        }
        return ans;
    }
    public static List<Integer> partitionLabels(String s) {
        int len = s.length();
        int[]last=new int[26];
        for (int i = 0; i < s.length(); i++) {
            last[s.charAt(i)-'a']=i;
        }
        int start=0;
        int end=0;
        List<Integer> ans=new ArrayList<>();
        for (int i = 0; i < len; i++) {
            end=Math.max(end,last[s.charAt(i)-'a']);
            if (end==i){
                ans.add(end-start+1);
                start=end+1;
            }
        }
        return ans;
    }

    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer,Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num,map.getOrDefault(num, 0)+1);
        }
        PriorityQueue<int[]>queue = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return b[1]-a[1];
            }
        });
        for (Integer i : map.keySet()) {
            queue.add(new int[]{i,map.get(i)});
        }
        int[]ans=new int[k];
        for (int i = 0; i < k; i++) {
            ans[i]=queue.poll()[0];
        }
        return ans;
    }


    public static int findKthLargest(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length - k];
    }

    public static int longestValidParentheses(String s) {
        int[]dp=new int[s.length()];
        int ans=0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i)==')' && i!=0){
                int before = dp[i - 1];
                if (i-before-1>=0 && s.charAt(i-before-1)=='('){
                    dp[i]=before+2;
                    if (i-before-2>=0 ){
                        dp[i]+=dp[i-before-2];
                    }
                }
                ans=Math.max(ans,dp[i]);
            }
        }
        return ans;
    }
    public static boolean canPartition(int[] nums) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        if (sum % 2 != 0) {
            return false;
        }
        int target = sum / 2;
        // 使用布尔数组，dp[i] 表示是否能组成和为 i
        boolean[] dp = new boolean[target + 1];
        dp[0] = true; // 不选任何元素就能组成和为 0

        for (int num : nums) {
            // 从后往前遍历，防止重复使用同一个元素
            for (int j = target; j >= num; j--) {
                dp[j] = dp[j] || dp[j - num];
            }
        }
        return dp[target];
    }

    public static  boolean canPartition1(int[] nums) {
        int sum=0;
        for (int num : nums) {
            sum+=num;
        }
        if (sum%2!=0){
            return false;
        }
        int target = sum / 2;
        Arrays.sort(nums);
        int[]dp=new int[target+1];
        for (int i = 1; i <= target; i++) {
            dp[i]=Integer.MAX_VALUE;
            for (int num : nums) {
                if (i>=num && dp[i-num]!=Integer.MAX_VALUE){
                    dp[i]=Math.min(dp[i],dp[i-num]+1);
                }
            }
        }
        return dp[target]!=Integer.MAX_VALUE;
    }

    public int maxProduct(int[] nums) {
        int []arr=new int[nums.length];
        int []brr=new int[nums.length];
        arr[0]=nums[0];
        brr[0]=nums[0];
        int ans=arr[0];
        for (int i = 1; i < nums.length; i++) {
            arr[i]=Math.max(arr[i-1]*nums[i],Math.max(nums[i],nums[i]*brr[i-1]));
            brr[i]=Math.min(brr[i-1]*nums[i],Math.min(nums[i],nums[i]*arr[i-1]));
            ans=Math.max(ans,arr[i]);
        }
        return ans;
    }

    public static int lengthOfLIS(int[] nums) {
        int len = nums.length;
        if(len==0){
            return 0;
        }
        int[]dp=new int[nums.length];
        dp[nums.length-1]=1;
        int ans=1;
        for (int i = nums.length-2; i >=0; i--) {
            dp[i]=1;
            for (int j = i+1; j <nums.length ; j++) {
                if (nums[j]>nums[i]){
                    dp[i]=Math.max(dp[i],dp[j]+1);
                }
            }
            ans=Math.max(ans,dp[i]);
        }
        return ans;
    }

    public static boolean wordBreak(String s, List<String> wordDict) {
        int maxlen=0;
        Set<String> set=new HashSet<>();
        int []dp=new int[s.length()+1];
        Arrays.fill(dp,-1);
        for (String string : wordDict) {
            maxlen=Math.max(maxlen,string.length());
        }
        return dfs(s,0,wordDict,maxlen,dp)==1;
    }

    public static int dfs(String s, int i,List<String>wordDict,int maxlen,int[]dp){
        if (i==s.length()){
            return 1;
        }
        if (dp[i]!=-1){
            return dp[i];
        }
        for (int j = i+1; j <Math.min(s.length(),j+maxlen)+1 ; j++) {
            if (wordDict.contains(s.substring(i,j) ) && dfs(s,j,wordDict,maxlen,dp)==1){
                return dp[i]=1;
            }
        }
        return dp[i]=0;
    }


    public static int coinChange(int[] coins, int amount) {
        int[]dp=new int[amount+1];
        Arrays.sort(coins);
        dp[0]=0;
        for (int i = 1; i <= amount; i++) {
            dp[i]=Integer.MAX_VALUE;
            for (int coin : coins) {
                if (i>=coin && dp[i-coin]!=Integer.MAX_VALUE){
                    dp[i]=Math.min(dp[i],dp[i-coin]+1);
                }
            }
        }
        return dp[amount]!=Integer.MAX_VALUE?dp[amount]:-1;
    }

}
