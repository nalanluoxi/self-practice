package likou;

import likou.entity.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：二叉树的后序遍历
 * @Date：2025/7/7 11:31
 * @Filename：二叉树的后序遍历
 */
public class 二叉树的后序遍历 {

    public static void main(String[] args) {

    }

    static List<Integer> ans;
    public List<Integer> postorderTraversal(TreeNode root) {
        ans=new ArrayList<>();
        dfs(root);
        return ans;
    }

    public static void dfs(TreeNode root){
        if (root==null){
            return;
        }
        dfs(root.left);
        dfs(root.right);
        ans.add(root.val);
    }





}
