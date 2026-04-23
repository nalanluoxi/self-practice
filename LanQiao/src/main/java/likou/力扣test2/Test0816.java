package likou.力扣test2;

import likou.entity.TreeNode;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：Test0816
 * @Date：2025/8/16 16:00
 * @Filename：Test0816
 */
public class Test0816 {

    public static void main(String[] args) {


    }

    static List<List<Integer>> ans;
    static List<Integer> tans;

    public List<List<Integer>> subsets(int[] nums) {

        ans = new ArrayList<>();
        tans = new ArrayList<>();
        for (int i = 0; i <= nums.length; i++) {
            dfs(nums, 0, i);
        }
        return ans;
    }
    public void dfs (int[]nums,int index,int len){
        if (tans.size()==len){
            ans.add(new ArrayList<>(tans));
            return;
        }
        for (int i = index; i < nums.length; i++) {
            tans.add(nums[i]);
            dfs(nums,i+1,len);
            tans.remove(tans.size()-1);
        }
    }

    public TreeNode sortedArrayToBST(int[] nums) {
        return helper(nums,0,nums.length-1);
    }

    public TreeNode helper(int[]nums,int left,int right){
        if (left>right){
            return null;
        }

        int mid = left + (right - left) / 2;
        TreeNode node=new TreeNode(nums[mid]);
        node.left=helper(nums,left,mid-1);
        node.right=helper(nums,mid+1,right);
        return node;
    }
    public List<List<Integer>> levelOrder(TreeNode root) {

        List<List<Integer>> ans=new ArrayList<>();
        List<Integer> tan=new ArrayList<>();
        Deque<TreeNode> stack=new LinkedList<>();
        if (root==null){
            return ans;
        }
        stack.addLast(root);
        while (stack.size()!=0){
            tan.clear();
            int size = stack.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = stack.pollFirst();
                tan.add(node.val);
                if (node.left!=null){
                    stack.addLast(node.left);
                }
                if (node.right!=null){
                    stack.addLast(node.right);
                }
            }
            ans.add(new ArrayList<>(tan));
        }
        return ans;
    }



}
