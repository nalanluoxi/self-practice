package likou.二分;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @Author 纳兰洛熙
 * @Package：likou.二分
 * @Project：LanQiaoBei
 * @name：二叉搜索树中第k小元素
 * @Date：2025/6/26 16:37
 * @Filename：二叉搜索树中第k小元素
 */
public class 二叉搜索树中第k小元素 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(3, new TreeNode(1, null, new TreeNode(2)), new TreeNode(4));
        System.out.println(kthSmallest(root,1));
    }

    //static PriorityQueue<Integer> queue;
    static Queue<Integer> queue;
    public static int kthSmallest(TreeNode root, int k) {
        queue=new LinkedList<>();
        //queue=new PriorityQueue<>();
        dfs(root);
        for (int i = 0; i < k-1; i++) {
            queue.poll();
        }
        return queue.peek();
    }

    public static void dfs(TreeNode root){
        if (root==null){
            return;
        }
        dfs(root.left);
        queue.offer(root.val);
        dfs(root.right);
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
