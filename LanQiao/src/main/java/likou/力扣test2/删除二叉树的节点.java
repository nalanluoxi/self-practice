package likou.力扣test2;

import likou.entity.TreeNode;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：删除二叉树的节点
 * @Date：2025/7/15 11:18
 * @Filename：删除二叉树的节点
 */
public class 删除二叉树的节点 {
    public static void main(String[] args) {

    }

    public static TreeNode deleteNode(TreeNode root, int key) {
        if (root==null){
            return root;
        }
        if (root.val > key) {
            root.left=deleteNode(root.left,key);
            return root;
        }else if (root.val<key){
            root.right=deleteNode(root.right,key);
            return root;
        } else if (root.val == key) {
            if (root.left==null&&root.right==null){
                return null;
            } else if (root.left == null && root.right != null) {
                return root.right;
            } else if (root.left != null && root.right == null) {
                return root.left;
            }else {
                TreeNode right = root.right;
                while (right.left!=null){
                    right=right.left;
                }
                root.right=deleteNode(root.right,right.val);
                right.right=root.right;
                right.left=root.left;
                return right;
            }
        }
        return root;
    }
}
