package likou;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：二叉树的最近公共祖先
 * @Date：2025/3/26 19:28
 * @Filename：二叉树的最近公共祖先
 */
public class 二叉树的最近公共祖先 {
    public static void main(String[] args) {
        TreeNode root=new TreeNode(3);
        root.left=new TreeNode(5);
        root.right=new TreeNode(1);
        root.left.left=new TreeNode(6);
        root.left.right=new TreeNode(2);
        root.right.left=new TreeNode(0);
        root.right.right=new TreeNode(8);
        root.left.right.left=new TreeNode(7);
        root.left.right.right=new TreeNode(4);

        TreeNode treeNode = lowestCommonAncestor(root, new TreeNode(5), new TreeNode(1));
        System.out.println(treeNode.val);

    }


    static HashMap<Integer, TreeNode> parent;
    static List<Integer> visited;

    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || p == null || q == null) {
            return null;
        }
        init(root);

        while (p!=null){
            visited.add(p.val);
            p = parent.get(p.val);
        }
        while (q!=null){
            if (visited.contains(q.val)){
                return q;
            }
            q = parent.get(q.val);
        }
        return null;
    }

    public static void init(TreeNode root) {
        parent = new HashMap<>();
        visited = new LinkedList<>();
        initHelp(root);
    }

    public static void initHelp(TreeNode root) {
        if (root == null) {
            return;
        }
        if (root.left != null) {
            parent.put(root.left.val, root);
            initHelp(root.left);
        }
        if (root.right != null) {
            parent.put(root.right.val, root);
            initHelp(root.right);
        }
    }


    public static  class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

}
