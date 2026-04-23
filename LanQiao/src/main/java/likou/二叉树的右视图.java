package likou;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：二叉树的右视图
 * @Date：2025/4/16 17:16
 * @Filename：二叉树的右视图
 */
public class 二叉树的右视图 {


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

    public static void main(String[] args) {
        TreeNode node1 = new TreeNode(1);
        TreeNode node2 = new TreeNode(2);
        TreeNode node3 = new TreeNode(3);
        TreeNode node4 = new TreeNode(4);
        TreeNode node5 = new TreeNode(5);
        node1.left = node2;
        node1.right = node3;
        node2.right = node5;
        node3.right = node4;
        List<Integer> integers = rightSideView(node1);
        for (Integer integer : integers) {
            System.out.println(integer);
        }


    }

    public static List<Integer> rightSideView(TreeNode root) {
        Deque<TreeNode> deque=new LinkedList<>();
        List<Integer> ans=new LinkedList<>();
        if (root==null){
            return ans;
        }
        deque.add(root);
        int temp=-1;
        while (!deque.isEmpty()){
            int size=deque.size();
            for (int i = 0; i < size; i++) {
                TreeNode treeNode = deque.pollFirst();
                temp=treeNode.val;
                if (treeNode.left!=null){
                    deque.add(treeNode.left);
                }
                if (treeNode.right!=null){
                    deque.add(treeNode.right);
                }
            }
            ans.add(temp);
        }
        return ans;
    }
}
