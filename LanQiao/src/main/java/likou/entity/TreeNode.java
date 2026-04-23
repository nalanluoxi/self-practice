package likou.entity;

/**
 * @Author 纳兰洛熙
 * @Package：likou.entity
 * @Project：LanQiaoBei
 * @name：TreeNode
 * @Date：2025/7/7 11:33
 * @Filename：TreeNode
 */

public class TreeNode {
   public int val;
   public TreeNode left;
   public TreeNode right;
   public TreeNode() {}
   public TreeNode(int val) { this.val = val; }
   public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}
