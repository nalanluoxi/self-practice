package likou;

import likou.entity.TreeNode;
import 蓝桥杯真题.决赛13届.内存空间;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：删除二叉搜索树中的节点
 * @Date：2025/7/8 15:54
 * @Filename：删除二叉搜索树中的节点
 */
public class 删除二叉搜索树中的节点 {
    public static void main(String[] args) {
        TreeNode n5=new TreeNode(5);
        TreeNode n3=new TreeNode(3);
        TreeNode n2=new TreeNode(2);
        TreeNode n4=new TreeNode(4);
        TreeNode n6=new TreeNode(6);
        TreeNode n7=new TreeNode(7);
        n5.left=n3;
        n5.right=n6;
        n6.right=n7;
        n3.left=n2;
        n3.right=n4;
        TreeNode treeNode = deleteNode(n5, 3);


    }


    public static TreeNode deleteNode(TreeNode root, int key) {
        if (root==null){
            return null;
        }
        if (root.val>key){
            root.left= deleteNode(root.left,key);
            return root;
        }
        if (root.val<key){
            root.right=deleteNode(root.right,key);
            return root;
        }
        if (root.val==key){
            if (root.left==null&&root.right==null){
                return null;
            } else if (root.left != null && root.right == null) {
                return root.left;
            } else if (root.left == null && root.right!=null){
                return root.right;
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
