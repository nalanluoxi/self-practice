package likou;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：二叉树的序列化和反序列化
 * @Date：2025/5/29 10:00
 * @Filename：二叉树的序列化和反序列化
 */
public class 二叉树的序列化和反序列化 {
    public static void main(String[] args) {
        /*TreeNode root=new TreeNode(1);
        root.left=new TreeNode(2);
        root.right=new TreeNode(3);
        root.right.left=new TreeNode(4);
        root.right.right=new TreeNode(5);
        Codec codec=new Codec();
        String serialize = codec.serialize(root);
        System.out.println(serialize);*/
        String s="1,2,3,null,null,4,5";
        //String s="";
        Codec codec=new Codec();
        TreeNode deserialize = codec.deserialize(s);
        System.out.println(deserialize.val);
    }

     public static class TreeNode {
         int val;
         TreeNode left;
         TreeNode right;
         TreeNode(int x) { val = x; }
     }

    public static class Codec {

        // Encodes a tree to a single string.
        public String serialize(TreeNode root) {
            String ans="";
            if (root==null){
                return ans;
            }
            return helpser(root,ans);
        }

        public String helpser(TreeNode root,String ans){
            if (root==null){
                ans+="null,";
            }else {
                ans+=root.val+",";
                ans+=helpser(root.left,ans);
                ans+=helpser(root.right,ans);
            }
            return ans;
        }

        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {
            String[] split = data.split(",");
            List<String> list = new LinkedList<>(Arrays.asList(split));
            return helpdes(list);
        }

        public TreeNode helpdes(List<String> list){
            if (list.size()==0||list.get(0).equals("")){
                return null;
            }
            if (list.get(0).equals("null")){
                list.remove(0);
                return null;
            }else {
                TreeNode root=new TreeNode(Integer.parseInt(list.get(0)));
                list.remove(0);
                root.left=helpdes(list);
                root.right=helpdes(list);
                return root;
            }
        }
    }

   /* public static class Codec {

        // Encodes a tree to a single string.
        public String serialize(TreeNode root) {
            String ans="";
            Deque<TreeNode> deque=new LinkedList<>();
            deque.addLast(root);
            while (!deque.isEmpty()&&check(deque)){
                TreeNode node=deque.pollFirst();
                if (node==null){
                    ans+="null,";
                }else {
                    ans+=node.val+",";
                    deque.addLast(node.left);
                    deque.addLast(node.right);
                }
            }
            return ans;
        }
        private boolean check(Deque<TreeNode> deque){
            int bo=1;
            for (TreeNode treeNode : deque) {
                if (treeNode!=null){
                    bo*=0;
                }
            }
            return bo==0;
        }

        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {
            String[] split = data.split(",");
            if (split.length==0||data.equals("")){
                return null;
            }
            TreeNode root=new TreeNode(Integer.parseInt(split[0]));
            Deque<TreeNode> deque=new LinkedList<>();
            deque.addLast(root);
            int i=1;
            while (!deque.isEmpty()&&i<split.length){
                TreeNode node = deque.pollFirst();
                if (node==null){
                    continue;
                }
                if(!split[i].equals("null")){
                    node.left=new TreeNode(Integer.parseInt(split[i++]));
                    deque.addLast(node.left);
                }else {
                    node.left=null;
                    deque.addLast(node.left);
                    i++;
                }
                if (i>=split.length){
                    break;
                }
                if(!split[i].equals("null")){
                    node.right=new TreeNode(Integer.parseInt(split[i++]));
                    deque.addLast(node.right);
                }else {
                    node.right=null;
                    deque.addLast(node.right);
                    i++;
                }
            }
            return root;
        }
    }*/

}
