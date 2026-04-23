package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：平衡二叉树
 * @Date：2025/5/4 22:24
 * @Filename：平衡二叉树
 */
public class 平衡二叉树 {
    public static void main(String[] args) {

    }
    public static boolean isBalanced(TreeNode root) {
        if (root==null){
            return true;
        }
        return helper(root.left)-helper(root.right)<=1&&helper(root.left)-helper(root.right)>=-1&&isBalanced(root.left)&&isBalanced(root.right);
    }

    public static int helper(TreeNode root){
        if (root==null){
            return 0;
        }
        return Math.max(helper(root.left),helper(root.right))+1;
    }

  public class TreeNode {
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

}
