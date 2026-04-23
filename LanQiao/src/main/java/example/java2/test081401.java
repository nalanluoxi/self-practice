package example.java2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2
 * @Project：LanQiaoBei
 * @name：test081401
 * @Date：2025/8/14 19:34
 * @Filename：test081401
 */
public class test081401 {




     static class TreeNode {
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
        TreeNode n1=new TreeNode(1);
        TreeNode n2=new TreeNode(2);
        TreeNode n3=new TreeNode(3);
        TreeNode n4=new TreeNode(4);
        TreeNode n5=new TreeNode(5);
        TreeNode n6=new TreeNode(6);
        n1.left=n2;
        n1.right=n3;
        n3.right=n4;
        n4.left=n5;
        n4.right=n6;
        flatten(n1);
        while (n1!=null){
            System.out.println(n1.val);
            n1=n1.right;
        }
    }

     static List<TreeNode> list ;
    public static void flatten(TreeNode root) {
        // write your code here.
        if (root==null){
            return;
        }
        list=new ArrayList<>();
        dfs(root);
        list.remove(0);
        TreeNode cur=root;
        int i=0;
        while (i<list.size()){
            cur.left=null;
            cur.right=list.get(i);
            i++;
            cur=cur.right;
        }
    }

    public static void dfs(TreeNode root){
        if (root==null){
            return;
        }
        list.add(new TreeNode(root.val));
        dfs(root.left);
        dfs(root.right);
    }


}
