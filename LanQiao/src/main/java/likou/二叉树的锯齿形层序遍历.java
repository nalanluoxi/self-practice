package likou;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：二叉树的锯齿形层序遍历
 * @Date：2025/3/24 16:39
 * @Filename：二叉树的锯齿形层序遍历
 */
public class 二叉树的锯齿形层序遍历 {
    public static void main(String[] args) {
       /* TreeNode root = new TreeNode(3);
        TreeNode root1 = new TreeNode(9);
        TreeNode root2 = new TreeNode(20);
        TreeNode root3 = new TreeNode(15);
        TreeNode root4 = new TreeNode(7);
        root.left=root1;
        root.right=root2;
        root2.left=root3;
        root2.right=root4;*/
        TreeNode root1 = new TreeNode(1);
        TreeNode root2 = new TreeNode(2);
        TreeNode root3 = new TreeNode(3);
        TreeNode root4 = new TreeNode(4);
        TreeNode root5 = new TreeNode(5);
        root1.left = root2;
        root1.right = root3;
        root2.left = root4;
        root3.right = root5;


        List<List<Integer>> lists = zigzagLevelOrder(root1);
        for (List<Integer> list : lists) {
            System.out.print("[ ");
            for (Integer last : list) {
                System.out.print(last + " ");
            }
            System.out.println("]");
        }
    }

    public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        Deque<TreeNode> deque = new LinkedList<>();
        Deque<Integer> tans = new LinkedList<>();
        if (root == null) {
            return ans;
        }
        deque.offer(root);
        int isLeft = 1;
        while (!deque.isEmpty()) {
            int size = deque.size();
            tans.clear();
            for (int i = 0; i < size; i++) {
                TreeNode now = deque.poll();
                if (isLeft == 1) {
                    tans.offerLast(now.val);
                }else {
                    tans.offerFirst(now.val);
                }
                if (now.left != null) {
                    deque.offer(now.left);
                }
                if (now.right != null) {
                    deque.offer(now.right);
                }
            }
            ans.add(new LinkedList<>(tans));
            isLeft*=-1;
        }
        return ans;
    }

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


}
