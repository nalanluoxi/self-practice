package likou;



import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：二叉树展开为链表
 * @Date：2025/7/7 9:31
 * @Filename：二叉树展开为链表
 */
public class 二叉树展开为链表 {



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
        TreeNode node1=new TreeNode(1);
        TreeNode node2=new TreeNode(2);
        TreeNode node3=new TreeNode(3);
        TreeNode node4=new TreeNode(4);
        TreeNode node5=new TreeNode(5);
        TreeNode node6=new TreeNode(6);
        node1.left=node2;
        node1.right=node5;
        node2.left=node3;
        node2.right=node4;
        node5.right=node6;
        flatten(node1);
        TreeNode cur=node1;
        while (cur!=null){
            System.out.println(cur.val);
            cur=cur.right;
        }
    }

    static List<TreeNode> list;
    public static void flatten(TreeNode root) {
        list=new ArrayList<>();
        dfs(root);
        for (int i = 0; i < list.size(); i++) {
            TreeNode node = list.get(i);
            node.left=null;
            node.right=null;
            if (i+1!=list.size()){
                node.right=list.get(i+1);
            }
        }
    }

    public static void dfs(TreeNode root){
        if (root==null){
            return;
        }
        list.add(root);
        dfs(root.left);

        dfs(root.right);
    }
}
