package likou.力扣test2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：二叉树的中序遍历
 * @Date：2025/6/4 20:27
 * @Filename：二叉树的中序遍历
 */
public class 二叉树的中序遍历 {


    static List<Integer> ans;
    public List<Integer> inorderTraversal(TreeNode root) {
        ans=new ArrayList<>();
        dps(root);
        return ans;
    }

    public static void dps(TreeNode root){
        if (root==null){
            return;
        }
        dps(root.left);
        ans.add(root.val);
        dps(root.right);
    }


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

}
