package likou;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：二叉树最大宽度
 * @Date：2025/5/12 9:21
 * @Filename：二叉树最大宽度
 */
public class 二叉树最大宽度 {
    public static void main(String[] args) {
        TreeNode root=new TreeNode(1);
        root.left=new TreeNode(3);
        root.right=new TreeNode(2);
        root.left.left=new TreeNode(5);
      /*  root.left.right=new TreeNode(3);
        root.right.right=new TreeNode(9);*/
        System.out.println(widthOfBinaryTree(root));
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

    static int ans;
    static List<Pair> map;
    public static int widthOfBinaryTree(TreeNode root) {
        ans=0;
        if(root==null){
            return 0;
        }

        map=new ArrayList<>();
        map.add(new Pair(root,0));
        while(!map.isEmpty()){
            List<Pair> temp=new ArrayList<>();
            for(Pair pair:map){
                TreeNode node=pair.node;
                int index=pair.index;
                if(node.left!=null){
                    temp.add(new Pair(node.left,index*2));
                }
                if(node.right!=null){
                    temp.add(new Pair(node.right,index*2+1));
                }
            }
            ans=Math.max(ans,map.get(map.size()-1).index-map.get(0).index+1);
            map=temp;
        }
        return ans;
    }
    static class Pair{
        TreeNode node;
        int index;

        public Pair(TreeNode node, int index) {
            this.node = node;
            this.index = index;
        }
    }

}
