package likou;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：从前序与中序遍历构造二叉树
 * @Date：2025/4/27 9:12
 * @Filename：从前序与中序遍历构造二叉树
 */
public class 从前序与中序遍历构造二叉树 {
    public static void main(String[] args) {
        int[] preorder = {3,9,20,15,7};
        int[] inorder = {9,3,15,20,7};
        TreeNode ans = buildTree(preorder,inorder);
        System.out.println(ans.val);
    }

    static int[] pre;
    static int[] in;
    static HashMap<Integer,Integer> map;
    public static TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder.length == 0) {
            return null;
        }
        pre = preorder;
        in = inorder;
        map = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i],i);
        }
        return help(0,preorder.length-1,0,inorder.length-1);
    }

    public static TreeNode help(int preleft,int preright,int inleft,int inright) {
        if (preleft > preright) {
            return null;
        }
        int inroot = map.get(pre[preleft]);
        int preroot=preleft;
        TreeNode root = new TreeNode(pre[preroot]);
        int leftsize=inroot-inleft;
        root.left=help(preleft+1,preleft+leftsize,inleft,inroot-1);
        root.right=help(preleft+1+leftsize,preright,inroot+1,inright);
        return root;
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
