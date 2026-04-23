package likou;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：求根节点到叶节点数字之和
 * @Date：2025/4/27 10:28
 * @Filename：求根节点到叶节点数字之和
 */
public class 求根节点到叶节点数字之和 {
    public static void main(String[] args) {
        TreeNode node1 = new TreeNode(4);
        TreeNode node2 = new TreeNode(9);
        TreeNode node3 = new TreeNode(5);
        TreeNode node4 = new TreeNode(1);
        TreeNode node5 = new TreeNode(0);
        node1.left = node2;
        node1.right = node5;
        node2.left = node3;
        node2.right = node4;
        System.out.println(sumNumbers(node1));
    }

    static Deque<TreeNode> deque;
    public static int sumNumbers(TreeNode root) {
        if (root==null){
            return 0;
        }
        deque=new LinkedList<>();
        deque.add(root);
        ans=0;
        help(root);
        return ans;
    }

    public static void help(TreeNode root) {
        if (root.left==null && root.right==null){
            add();
            return;
        }
        if (root.left!=null){
            deque.add(root.left);
            help(root.left);
            deque.removeLast();
        }
        if (root.right!=null){
            deque.add(root.right);
            help(root.right);
            deque.removeLast();
        }
        return;
    }

    static int ans;
    public static void add() {
        int temp=0;
        for (TreeNode treeNode : deque) {
            temp=temp*10+ treeNode.val;
        }
        System.out.println(temp);
        ans+=temp;
        System.out.println("ans: "+ans);
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
