package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：二叉树最大深度
 * @Date：2025/5/3 21:05
 * @Filename：二叉树最大深度
 */
public class 二叉树最大深度 {


    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }


    public static int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return helper(root);
    }

    public static int helper(TreeNode root){
        if (root==null){
            return 0;
        }
        int left=helper(root.left);
        int right=helper(root.right);
        return Math.max(left,right)+1;
    }
}
