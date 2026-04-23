package likou;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：二叉树的中序遍历
 * @Date：2025/4/16 17:24
 * @Filename：二叉树的中序遍历
 */
public class 二叉树的中序遍历 {

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
        TreeNode node2 = new TreeNode(2);
        TreeNode node3 = new TreeNode(3);
        TreeNode node4 = new TreeNode(4);
        TreeNode node5 = new TreeNode(5);
        node1.left = node2;
        node1.right = node3;
        node2.left = node4;
        node2.right = node5;

        List<Integer> integers = inorderTraversal(node1);
        for (Integer integer : integers) {
            System.out.println(integer);
        }
    }

   /* public  static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<Integer>();
        Deque<TreeNode> stk = new LinkedList<TreeNode>();
        while (root != null || !stk.isEmpty()) {
            while (root != null) {
                stk.push(root);
                root = root.left;
            }
            root = stk.pop();
            res.add(root.val);
            root = root.right;
        }
        return res;
    }*/

    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        if (root == null) {
            return ans;
        }
        Deque<TreeNode> deque = new LinkedList<>();
        while (root != null || !deque.isEmpty()) {
            while (root != null) {
                deque.offerLast(root);
                root = root.left;
            }
            TreeNode last = deque.pollLast();
            ans.add(last.val);
            root = last.right;
        }

        return ans;
    }



   /* public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer>ans=new ArrayList<>();
        if (root==null){
            return ans;
        }
        help(root,ans);
        return ans;
    }

    public static void help(TreeNode root,List<Integer>ans){
        if (root==null){
            return;
        }
        help(root.left,ans);
        ans.add(root.val);
        help(root.right,ans);
    }*/
}
