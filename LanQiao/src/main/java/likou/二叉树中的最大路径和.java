package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：二叉树中的最大路径和
 * @Date：2025/4/16 19:12
 * @Filename：二叉树中的最大路径和
 */
public class 二叉树中的最大路径和 {

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }


    public static void main(String[] args) {
        TreeNode node1 = new TreeNode(1);
        TreeNode node2 = new TreeNode(-2);
        TreeNode node3 = new TreeNode(-3);
        TreeNode node4 = new TreeNode(1);
        TreeNode node5 = new TreeNode(3);
        TreeNode node6 = new TreeNode(-2);
        TreeNode node7 = new TreeNode(-1);
        node1.left = node2;
        node1.right = node3;
        node2.left = node4;
        node2.right = node5;
        node3.left = node6;
        node4.left=node7;
        System.out.println(maxPathSum(node1));

    }

    static int ans;
    public static int maxPathSum(TreeNode root) {
        ans = Integer.MIN_VALUE;
        help(root);
        return ans;
    }

   public static int help(TreeNode root) {
        if (root == null) {
            return 0;
        }
       int left = Math.max(0,help(root.left));
       int right = Math.max(0,help(root.right));
       ans=Math.max(ans,left+right+root.val);
       return root.val+Math.max(left,right);
   }


   /* public static int help(TreeNode root, List<Integer> list) {
        if (root == null) {
            return 0;
        }
        int left = help(root.left, list);
        int right=help(root.right, list);
        left=left+root.val;
        right=right+root.val;
        int max = Math.max(left, right);
        list.add(max);
        ans=Math.max(ans,max);
        return max;
    }
*/

}
