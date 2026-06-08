package Test2;

import likou.entity.ListNode;
import likou.entity.TreeNode;
import likou.判断子序列;

import java.util.*;

/**
 * @author nalan_luoxi
 * @version 1.0
 * @email adrian0304@qq.com
 * @project self-practice
 * @package Test2
 * @date 2026-06-05 17:21
 */
public class Test0605 {


    public static void main(String[] args) {
        System.out.println(isValid("([])"));
    }



    public static boolean isValidBST(TreeNode root) {
        if (root==null){
            return true;
        }
        boolean b=true;
        if (root.left!=null){
            if (root.left.val<root.val){
                b=b && isValidBST(root.left);
            }else {
                return false;
            }
        }
        if (root.right!=null){
            if (root.right.val>root.val){
                b=b && isValidBST(root.right);
            }else {
                return false;
            }
        }
        return b;
    }



    public static boolean isBalanced(TreeNode root) {
        if (root==null){
            return true;
        }
        return Math.abs(help(root.left)-help(root.right))<=1 && isBalanced(root.left) && isBalanced(root.right);
    }

    public static int help(TreeNode root){
        if (root==null){
            return 0;
        }
        return Math.max(help(root.left),help(root.right))+1;
    }


    public static boolean isValid(String s) {
        if (s.length()==0 || s.length()%2!=0){
            return false;
        }
        Map<String, String> map = Map.of("(", ")", "{", "}", "[", "]");
        Deque<String> deque=new LinkedList<>();
        for (int i = 0; i < s.length(); i++) {
            String c = s.charAt(i) + "";
            if (map.containsKey(c)){
                deque.addLast(map.get(c));
            }else {
                if (!deque.isEmpty() && deque.peekLast().equals(c)){
                    deque.removeLast();
                }else {
                    return false;
                }
            }
        }
        if (deque.isEmpty()){
            return true;
        }
        return false;
    }


    public static void reorderList(ListNode head) {
        List<ListNode> list=new ArrayList<>();
        ListNode cur=head;
        while (cur!=null){
            list.add(cur);
            cur=cur.next;
        }
        ListNode ans=new ListNode();
        ListNode temp=ans;
        int i=0,j=list.size()-1;
        while (i<j){
            temp.next=list.get(i++);
            temp=temp.next;
            if (i!=j){
                temp.next=list.get(j--);
                temp=temp.next;
            }
        }

        if (i==j){
            temp.next=list.get(i);
            temp=temp.next;
            temp.next=null;
        }
        head=ans.next;

    }
}
