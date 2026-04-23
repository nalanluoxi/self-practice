package likou;

import likou.entity.TreeNode;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：另一棵树的子树
 * @Date：2025/7/7 11:38
 * @Filename：另一棵树的子树
 */
public class 另一棵树的子树 {

    public static boolean isSubtree(TreeNode root, TreeNode subRoot) {
        return (root!=null&&subRoot!=null)&&(help(root,subRoot)||isSubtree(root.left,subRoot)||isSubtree(root.right, subRoot));
    }
    public static boolean help(TreeNode a,TreeNode b){
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null || a.val != b.val) {
            return false;
        }
        return help(a.left,b.left)&&help(a.right,b.right);
    }


}
