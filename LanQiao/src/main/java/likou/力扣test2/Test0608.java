package likou.力扣test2;

import likou.entity.TreeNode;

import java.util.*;

/**
 * @author nalan_luoxi
 * @version 1.0
 * @email adrian0304@qq.com
 * @project self-practice
 * @package likou.力扣test2
 * @date 2026-06-08 10:25
 */
public class Test0608 {
    public static void main(String[] args) {
        /*TreeNode root=new TreeNode(5);
        root.left=new TreeNode(4);
        root.right=new TreeNode(8);
        root.left.left=new TreeNode(11);
        root.left.left.left=new TreeNode(7);
        root.left.left.right=new TreeNode(2);
        root.right.left=new TreeNode(13);
        root.right.right=new TreeNode(4);
        root.right.right.right=new TreeNode(1);*/

      //  System.out.println(canFinish(root,22));

       //int[][]nums={{3,4,5},{3,2,6},{2,2,1}};
        int[][]nums={{9,9,4},{6,6,8},{2,1,1}};
        System.out.println(longestIncreasingPath(nums));

    }


    public static int longestIncreasingPath(int[][] nums) {
        int n = nums.length;
        int m = nums[0].length;
        int[][]dp=new int[n][m];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i],-1);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                dfs5(nums,i,j,dp);
            }
        }

        int max=0;
        for (int[] ints : dp) {
            for (int anInt : ints) {
                max=Math.max(max,anInt);
            }
        }
        return max;
    }

    public static int dfs5(int[][]nums,int i,int j,int[][]dp){
        if (dp[i][j]!=-1){
            return dp[i][j];
        }
        dp[i][j]=1;
        int[][]dirs={{0,1},{0,-1},{1,0},{-1,0}};
        for (int[] dir : dirs) {
            int x=i+dir[0];
            int y=j+dir[1];
            if (check(nums,x,y) && nums[x][y]<nums[i][j]){
                dp[i][j]=Math.max(dp[i][j],dfs5(nums,x,y,dp)+1);
            }
        }
        return dp[i][j];
    }

    public static boolean check(int[][]nums,int i,int j){
        if (i<0||i>=nums.length||j<0||j>=nums[0].length){
            return false;
        }
        return true;
    }


    public static boolean canFinish(int num, int[][] arr) {
        List<List<Integer>> list=new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(new ArrayList<>());
        }
        for (int[] tem : arr) {
            list.get(tem[1]).add(tem[0]);
        }
        int[]visited =new int[num];
        for (int i = 0; i < num; i++) {
            if (visited[i]!=0){
                continue;
            }else if (visited[i]==0 && !dfs4(list,i,visited)){
                return false;
            }
        }
        return true;
    }

    public static boolean dfs4(List<List<Integer>> list,int cur,int[] visited){
        visited[cur]=1;
        for (Integer i : list.get(cur)) {
            if (visited[i]==1){
                return false;
            }else if (visited[i]==0 && !dfs4(list,i,visited)){
                return false;
            }
        }
        visited[cur]=2;
        return true;
    }


    public static boolean hasPathSum(TreeNode root, int targetSum) {
        if (root==null){
            return false;
        }
        return dfs3(root,targetSum,0);
    }

    public static boolean dfs3(TreeNode root,int target,int sum){
        sum+=root.val;
        if (root.left==null && root.right==null){
            return target==sum;
        }
        boolean l=false;
        boolean r=false;
        if (root.left!=null &&dfs3(root.left,target,sum)){
            return true;
        }
        if (root.right!=null && dfs3(root.right,target,sum)){
            return true;
        }
        return false;
    }

    public static List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        List<List<Integer>>ans=new ArrayList<>();
        List<Integer>list=new ArrayList<>();
        if (root==null){
            return ans;
        }
        dfs2(root,list,targetSum,0,ans);
        return ans;
    }

    public static void dfs2(TreeNode root,List<Integer>list,int targetSum,int nowSum,List<List<Integer>>ans){
        list.add(root.val);
        nowSum+=root.val;
        if (root.left==null && root.right==null){
            if (targetSum==nowSum){
                ans.add(new ArrayList<>(list));
                //return;
            }
            list.remove(list.size()-1);
            return;
        }
        //list.add(root.val);
        if (root.left!=null){
            dfs2(root.left,list,targetSum,nowSum,ans);
        }
        if (root.right!=null){
            dfs2(root.right,list,targetSum,nowSum,ans);
        }
        list.remove(list.size()-1);
    }


    public static boolean isValidBST(TreeNode root) {
       return dfs1(root,Long.MIN_VALUE,Long.MAX_VALUE);
    }

    public static boolean dfs1(TreeNode root,long min,long max){
        if (root==null){
            return true;
        }
        if (root.val<=min || root.val >=max){
            return false;
        }
        return dfs1(root.left,min,root.val) && dfs1(root.right,root.val,max);

    }
}
