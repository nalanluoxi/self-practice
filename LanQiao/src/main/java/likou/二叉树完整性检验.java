package likou;

import com.sun.source.tree.Tree;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：二叉树完整性检验
 * @Date：2025/7/3 22:15
 * @Filename：二叉树完整性检验
 */
public class 二叉树完整性检验 {
    public static void main(String[] args) {
        TreeNode node1=new TreeNode(1);
        TreeNode node2=new TreeNode(2);
        TreeNode node3=new TreeNode(3);
        TreeNode node4=new TreeNode(4);
        TreeNode node5=new TreeNode(5);
        //TreeNode node6=new TreeNode(6);
        TreeNode node7=new TreeNode(7);
        TreeNode node8=new TreeNode(8);

        node1.left=node2;
        node1.right=node3;
        node2.left=node5;
        node3.left=node7;
        node3.right=node8;
        //node2.left=node4;
        //node2.right=node5;
        //node3.left=node6;

        System.out.println(isCompleteTree(node1));

    }

    public static boolean isCompleteTree(TreeNode root) {
        Deque<TreeNode> queue=new LinkedList<>();
        boolean ans=false;
        queue.offerLast(root);
        while (!queue.isEmpty()){
            TreeNode node = queue.pollFirst();
            if (node!=null){
                if (ans){
                    return false;
                }
                queue.offerLast(node.left);
                queue.offerLast(node.right);
            }else {
                ans=true;
            }
        }
        return true;
    }
/*
    static boolean ans;
    public static boolean isCompleteTree(TreeNode root) {
        ans=true;
        dfs(root);
        return ans;
    }*/

   /* public static void dfs(TreeNode root){
        if (!ans){
            return;
        }
        if (root.left==null&&root.right!=null){
            ans=false;
            return;
        }
        if (root.left!=null){
            dfs(root.left);
        }
        if (root.right!=null){
            dfs(root.right);
        }
    }
*/


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
