package likou.力扣test2;

import likou.entity.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.*;

/**
 * @author nalan_luoxi
 * @version 1.0
 * @email adrian0304@qq.com
 * @project self-practice
 * @package likou.力扣test2
 * @date 2026-06-09 15:49
 */
public class Test0609 {




    public static TreeNode buildTree(int[] inorder, int[] postorder) {
                                            //左中右            //左右中
        Map<Integer,Integer> map=new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i],i);
        }



        return null;
    }

    public static TreeNode dfs3(TreeNode root){
        return null;
    }



    public static void flatten(TreeNode root) {
        List<TreeNode>list=new ArrayList<>();
        dfs2(root,list);
        for (int i = 0; i < list.size(); i++) {
            TreeNode now = list.get(i);
            now.left=null;
            if (i+1<list.size()){
                now.right=list.get(i+1);
            }else {
                now.right=null;
            }
        }
    }

    public static void dfs2(TreeNode root,List<TreeNode>list){
        if (root==null){
            return;
        }
        list.add(root);
        dfs2(root.left,list);
        dfs2(root.right,list);
    }

    public static int longestIncreasingPath(int[][] nums) {
        int n = nums.length;
        int m = nums[0].length;
        int[][]dp=new int[n][m];

        int max=1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                max=Math.max(max,dfs1(nums,dp,i,j));
            }
        }
        return max;
    }

    public static int dfs1(int[][]nums,int[][]dp,int i,int j){
        if (!check(nums,i,j)){
            return 0;
        }
        if (dp[i][j]!=0){
            return dp[i][j];
        }
        int[][]dirs={{0,1},{0,-1},{1,0},{-1,0}};
        dp[i][j]=1;
        for (int[] dir : dirs) {
            int x=i+dir[0];
            int y=j+dir[1];
            if (check(nums,x,y) && nums[x][y]>nums[i][j]){
                dp[i][j]=Math.max(dp[i][j],dfs1(nums,dp,x,y)+1);
            }
        }
        return dp[i][j];
    }

    public static boolean check(int[][]nums,int i,int j){
        if (i<0|| i>=nums.length|| j<0|| j>=nums[0].length){
            return false;
        }
        return true;
    }
}
