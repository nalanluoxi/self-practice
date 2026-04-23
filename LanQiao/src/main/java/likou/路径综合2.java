package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：路径综合2
 * @Date：2025/5/10 20:41
 * @Filename：路径综合2
 */
public class 路径综合2 {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(5);
        TreeNode node1 = new TreeNode(4);
        TreeNode node2 = new TreeNode(8);
        TreeNode node3 = new TreeNode(11);
        TreeNode node4 = new TreeNode(13);
        TreeNode node5 = new TreeNode(4);
        TreeNode node6 = new TreeNode(7);
        TreeNode node7 = new TreeNode(2);
        TreeNode node8 = new TreeNode(5);
        TreeNode node9 = new TreeNode(1);
        root.left = node1;
        root.right = node2;
        node1.left = node3;
        node2.left = node4;
        node2.right = node5;
        node3.left = node6;
        node3.right = node7;
        node5.left = node8;
        node5.right = node9;
        System.out.println(pathSum(root, 22));
       /* TreeNode root = new TreeNode(-2);
        TreeNode node1 = new TreeNode(-3);
        root.right = node1;
        System.out.println(pathSum(root, -5));
*/
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


    static List<List<Integer>> ans;
    static List<Integer> tans;
    static int target;
    public static List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        ans = new ArrayList<>();
        tans = new ArrayList<>();
        target=targetSum;
        if (root==null) {
            return ans;
        }
        tans.add(root.val);
        if (root!=null) {
            dfs(root, root.val);
        }
        return ans;
    }

    public static void dfs(TreeNode root, int sum) {
 /*       if (sum>target) {
            return;
        }*/
        if (root.left == null && root.right == null) {
            if (sum == target) {
                ans.add(new ArrayList<>(tans));
                return;
            }
        }
        if (root.left != null) {
            tans.add(root.left.val);
            dfs(root.left, sum+root.left.val);
            tans.remove(tans.size() - 1);
        }
        if (root.right != null) {
            tans.add(root.right.val);
            dfs(root.right, sum+root.right.val);
            tans.remove(tans.size() - 1);
        }
    }
}
