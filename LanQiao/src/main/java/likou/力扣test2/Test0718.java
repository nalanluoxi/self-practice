package likou.力扣test2;

import likou.entity.TreeNode;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：Test0718
 * @Date：2025/7/18 8:57
 * @Filename：Test0718
 */
public class Test0718 {
    public static void main(String[] args) {

    }

    public static int jump(int[] nums) {
        if (nums.length<=1){
            return 0;
        }
        int ans=0;
        int nowmax=0;
        int tman=0;
        for (int i = 0; i < nums.length; i++) {
            tman=Math.max(tman,i+nums[i]);
            if (i==nowmax){
                ans++;
                nowmax=tman;
                if (nowmax>=nums.length-1){
                    break;
                }
            }
        }
        return ans;
    }


    public static boolean canJump(int[] nums) {
        int c=0;
        for (int i = 0; i <= c; i++) {
            c=Math.max(c,i+nums[i]);
            if (c>=nums.length-1){
                return true;
            }
        }
        return false;
    }

    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer,Integer> map = new HashMap<>();
        for (int num : nums) {
            if (map.containsKey(num)){
                map.put(num,map.get(num)+1);
            }else {
                map.put(num,1);
            }
        }
        Queue<int[]> queue=new PriorityQueue<>((a,b)->a[1]-b[1]);
        for (Integer i : map.keySet()) {
            if (queue.size()<k){
                queue.add(new int[]{i,map.get(i)});
            }else {
                queue.add(new int[]{i,map.get(i)});
                queue.poll();
            }
        }
        int[]ans=new int[k];
        for (int i = 0; i < ans.length; i++) {
            ans[i]=queue.poll()[0];
        }
        return ans;
    }

    public static int[][] merge(int[][] nums) {
        Arrays.sort(nums,new Comparator<>(){
            @Override
            public int compare(int[] a, int[] b) {
                return a[0]-b[0];
            }
        });
        List<int[]>list=new ArrayList<>();
        list.add(new int[]{nums[0][0],nums[0][1]});
        for (int i = 1; i < nums.length; i++) {
            int[] ints = list.get(list.size() - 1);
            if (nums[i][0]>ints[1]){
                list.add(new int[]{nums[i][0],nums[i][1]});
            }else {
                list.remove(list.size()-1);
                int [] newints={Math.min(ints[0],nums[i][0])
                        ,Math.max(ints[1],nums[i][1])};
                list.add(newints);
            }
        }
        return list.toArray(new int[list.size()][]);
    }

    public static int numDecodings(String s) {
        if (s.length()<=0||s.charAt(0)=='0'){
            return 0;
        }
        int len = s.length();
        int []dp=new int[len+1];
        dp[0]=1;
        for (int i = 1; i <= len; i++) {
            if (s.charAt(i-1)!='0'){
                dp[i]=dp[i-1];
            }else {
                dp[i]=0;
            }
            if (i>1 && isOk(s.substring(i-2,i))){
                dp[i]+=dp[i-2];
            }
        }

        return dp[len];
    }

    public static boolean isOk(String s) {
        if (s.length() <= 0 || s.length() > 2 || s.charAt(0) == '0') {
            return false;
        }
        Integer i = Integer.valueOf(s);
        return i >= 1 && i <= 26;
    }


    public static int change(int amount, int[] coins) {
        if (amount == 0) {
            return 1;
        }
        int[] dp = new int[amount + 1];
        Arrays.sort(coins);
        dp[0] = 1;
        for (int coin : coins) {
            for (int i = 1; i <= amount; i++) {
                if (coin > i) {
                    continue;
                }
                dp[i] += dp[i - coin];
            }
        }
        return dp[amount];
    }

    public static int maxProfit(int[] nums) {
        int[][] dp = new int[nums.length][4];
        dp[0][0] = -nums[0];
        dp[0][2] = -nums[0];
        for (int i = 1; i < nums.length; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], -nums[i]);
            dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][0] + nums[i]);
            dp[i][2] = Math.max(dp[i - 1][2], dp[i - 1][1] - nums[i]);
            dp[i][3] = Math.max(dp[i - 1][3], dp[i - 1][2] + nums[i]);
        }
        return dp[nums.length - 1][3];
    }

    public static TreeNode deleteNode(TreeNode root, int key) {
        if (root == null) {
            return null;
        }
        if (root.val > key) {
            root.left = deleteNode(root.left, key);
            return root;
        } else if (root.val < key) {
            root.right = deleteNode(root.right, key);
            return root;
        } else {
            if (root.left == null && root.right == null) {
                return null;
            } else if (root.left == null && root.right != null) {
                return root.right;
            } else if (root.left != null && root.right == null) {
                return root.left;
            } else {
                TreeNode right = root.right;
                while (right.left != null) {
                    right = right.left;
                }
                root.right = deleteNode(root.right, right.val);
                right.right = root.right;
                right.left = root.left;
                return right;
            }
        }
    }
}
