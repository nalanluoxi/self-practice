package likou;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：对称二叉树
 * @Date：2025/5/3 20:48
 * @Filename：对称二叉树
 */
public class 对称二叉树 {

    public static void main(String[] args) {
        TreeNode root1=new TreeNode(1);
        TreeNode node2=new TreeNode(2);
        TreeNode node3=new TreeNode(2);
        TreeNode node4=new TreeNode(3);
        TreeNode node5=new TreeNode(4);
        TreeNode node6=new TreeNode(4);
        TreeNode node7=new TreeNode(3);
        root1.left=node2;
        root1.right=node3;
        node2.left=node4;
        node2.right=node5;
        node3.left=node6;
        node3.right=node7;
        System.out.println(isSymmetric(root1));

    }

    public static boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        return check(root.left, root.right);
    }


    public static boolean check(TreeNode left, TreeNode right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        return left.val == right.val && check(left.left, right.right) && check(left.right, right.left);
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
