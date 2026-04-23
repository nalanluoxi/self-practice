package likou.力扣test2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：二叉树找最近公共祖先
 * @Date：2025/6/6 14:03
 * @Filename：二叉树找最近公共祖先
 */
public class 二叉树找最近公共祖先 {
    public static void main(String[] args) {

    }

    static Map<TreeNode, TreeNode> parents;
    static List<TreeNode> visited;

    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || p == null || q == null) {
            return null;
        }
        parents = new HashMap<>();
        parents.put(root, null);
        init(root);
        visited = new ArrayList<>();
        while (p != null) {
            visited.add(p);
            p = parents.get(p);
        }
        while (!visited.contains(q)) {
            q = parents.get(q);
        }
        return q;
    }

    public static void init(TreeNode node) {
        if (node == null) {
            return;
        }
        if (node.left != null) {
            parents.put(node.left, node);
            init(node.left);
        }
        if (node.right != null) {
            parents.put(node.right, node);
            init(node.right);
        }
        return;
    }


    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

}
