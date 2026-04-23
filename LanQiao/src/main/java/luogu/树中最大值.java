package luogu;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：luogu
 * @Project：LanQiaoBei
 * @name：树中最大值
 * @Date：2025/3/17 16:02
 * @Filename：树中最大值
 */
public class 树中最大值 {
    public static void main(String[] args) {

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

    public static List<Integer> largestValues(TreeNode root) {
        List<Integer> ans=new ArrayList<>();
        Deque<TreeNode> deque=new ArrayDeque<>();
        if (root==null){
            return ans;
        }
        deque.add(root);
        int temmax=Integer.MIN_VALUE;
        while (!deque.isEmpty()){
            int size = deque.size();
            int tempmax=Integer.MIN_VALUE;
            while (size!=0){
                TreeNode nowNode = deque.pollFirst();
                size--;
                int val = nowNode.val;
                tempmax=Math.max(tempmax,val);
                if (nowNode.left!=null){
                    deque.add(nowNode.left);
                }
                if (nowNode.right!=null){
                    deque.add(nowNode.right);
                }
            }
            ans.add(tempmax);
        }
        return ans;
    }
}
