package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：二叉树的前序遍历
 * @Date：2025/5/4 20:09
 * @Filename：二叉树的前序遍历
 */
public class 二叉树的前序遍历 {
    public static void main(String[] args) {

    }


    static List<Integer> ans;
    public List<Integer> preorderTraversal(TreeNode root) {
        ans=new ArrayList<>();
        helper(root);
        return ans;
    }

    public static void helper(TreeNode root){
        if (root==null){
            return;
        }
        ans.add(root.val);
        helper(root.left);
        helper(root.right);
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
