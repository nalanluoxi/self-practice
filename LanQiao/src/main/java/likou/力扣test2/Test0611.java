package likou.力扣test2;

import likou.entity.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nalan_luoxi
 * @version 1.0
 * @email adrian0304@qq.com
 * @project self-practice
 * @package likou.力扣test2
 * @date 2026-06-11 11:50
 */
public class Test0611 {


    public static void main(String[] args) {
       /* int[] inorder = {9, 3, 15, 20, 7};
        int[] post = {9, 15, 7, 20, 3};
        TreeNode treeNode = buildTree(inorder, post);
        System.out.println(treeNode.val);*/


        int i=2;
        int[][]nums={{1,0}};
        System.out.println(canFinish(i,nums));
    }


    public static boolean canFinish(int num, int[][] nums) {
        List<List<Integer>> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(new ArrayList<>());
        }
        for (int i = 0; i < nums.length; i++) {
            list.get(nums[i][1]).add(nums[i][0]);
        }
        int[] dp = new int[num];
        for (int i = 0; i < num; i++) {
            if (dp[i] != 0) {
                continue;
            } else if (dp[i] == 0 && !dfs2(dp, i, list)) {
                return false;
            }
        }

        return true;
    }

    public static boolean dfs2(int[] dp, int i, List<List<Integer>> list) {

        dp[i] = 1;

        for (Integer integer : list.get(i)) {
            if (dp[integer] == 1) {
                return false;
            } else if (dp[integer] == 0 && !dfs2(dp, integer, list)) {
                return false;
            }
        }
        dp[i] = 2;
        return true;
    }

    //左中右   左右中
    public static TreeNode buildTree(int[] inorder, int[] postorder) {
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }
        TreeNode treeNode = dfs1(map, postorder, inorder.length - 1, 0, inorder.length - 1);
        return treeNode;
    }

    public static TreeNode dfs1(Map<Integer, Integer> map, int[] post, int cur, int left, int right) {
        if (left > right) {
            return null;
        }
        TreeNode root = new TreeNode(post[cur]);
        int now = post[cur];
        Integer i = map.get(now);
        root.right = dfs1(map, post, cur - 1, i + 1, right);
        root.left = dfs1(map, post, cur - (right - i) - 1, left, i - 1);
        return root;
    }


}
