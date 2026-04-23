package likou;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：二叉树的层序遍历
 * @Date：2025/3/21 11:09
 * @Filename：二叉树的层序遍历
 */
public class 二叉树的层序遍历 {
    public static void main(String[] args) {
        HashMap<Integer ,Integer> map = new HashMap<>();
    }

    static List<List<Integer>>ans;
    static  Deque<TreeNode> deque;
    static List<Integer> templist;
    public List<List<Integer>> levelOrder(TreeNode root) {
        ans=new ArrayList<>();
        if (root==null){
            return ans;
        }
        deque=new LinkedList<>();
        templist=new ArrayList<>();
        deque.add(root);
        while (!deque.isEmpty()){
            int size = deque.size();
            templist.clear();
            while (size>0){
                size--;
                TreeNode last = deque.pollLast();
                templist.add(last.val);
                if (last.left!=null){
                    deque.offerFirst(last.left);
                }
                if (last.right!=null){
                    deque.offerFirst(last.right);
                }
            }
            ans.add(new ArrayList<>(templist));
        }
        return ans;
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

}
