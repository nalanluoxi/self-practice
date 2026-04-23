package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：验证二叉搜索树
 * @Date：2025/5/6 10:15
 * @Filename：验证二叉搜索树
 */
public class 验证二叉搜索树 {
    public static void main(String[] args) {
        TreeNode node1 = new TreeNode(5);
        TreeNode node2 = new TreeNode(1);
        TreeNode node3 = new TreeNode(4);
        TreeNode node4 = new TreeNode(3);
        TreeNode node5 = new TreeNode(6);
        node1.left = node2;
        node1.right = node3;
        node3.left = node4;
        node3.right = node5;
        System.out.println(isValidBST(node1));

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

 /*   static ArrayList<Integer> list;

    public static boolean isValidBST(TreeNode root) {
        list = new ArrayList<>();
        helper(root);
        if (list.size() == 1) {
            return true;
        }
        Integer befor = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) <= befor) {
                return false;
            }
            befor = list.get(i);
        }
        return true;
    }

    public static void helper(TreeNode root) {
        if (root == null) {
            return;
        }
        helper(root.left);
        list.add(root.val);
        System.out.print(root.val+" , ");
        helper(root.right);
    }*/

/*    public static boolean isValidBST(TreeNode root) {
        if (root == null) {
            return true;
        }
        boolean left = true;
        boolean right = true;
        if (root.left != null) {
            if (root.left.val >= root.val) {
                return false;
            }
            left = isValidBST(root.left);
        }
        if (root.right != null) {
            if (root.right.val <= root.val) {
                return false;
            }
            right = isValidBST(root.right);
        }
        return left && right;
    }*/

    public static boolean isValidBST(TreeNode root) {
        if (root == null) {
            return true;
        }
        return helper(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }
    public static boolean helper(TreeNode root, long min, long max) {
        if (root == null) {
            return true;
        }
        if (root.val <= min || root.val >= max) {
            return false;
        }
        return helper(root.left,min, root.val)&& helper(root.right, root.val, max);
    }
}
