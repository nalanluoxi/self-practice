package likou;

import likou.entity.ListNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：排序奇升偶降链表
 * @Date：2025/7/8 9:41
 * @Filename：排序奇升偶降链表
 */
public class 排序奇升偶降链表 {
    public static void main(String[] args) {
        ListNode n1=new ListNode(1);
        ListNode n2=new ListNode(2);
        ListNode n3=new ListNode(3);
        ListNode n4=new ListNode(4);
        ListNode n5=new ListNode(5);
        ListNode n6=new ListNode(6);
        ListNode n7=new ListNode(7);
        ListNode n8=new ListNode(8);

        n1.next=n8;
        n8.next=n3;
        n3.next=n6;
        n6.next=n5;
        n5.next=n4;
        n4.next=n7;
        n7.next=n2;
        ListNode t1=n1;
        while (t1!=null){
            System.out.println(t1.val);
            t1=t1.next;
        }
        ListNode sort = sort(n1);
        while (sort!=null){
            System.out.println(sort.val);
            sort=sort.next;
        }
        //ListNode n1=new ListNode(1);
    }

    public static ListNode sort(ListNode root) {
        if (root==null||root.next==null||root.next.next==null){
            return root;
        }
        List<ListNode> odd=new ArrayList<>();
        List<ListNode> even=new ArrayList<>();
        ListNode cur=root;
        while (cur!=null){
            odd.add(cur);
            cur=cur.next;
            even.add(cur);
            if (cur==null){
                break;
            }
            cur=cur.next;
        }
        ListNode ans=new ListNode();
        cur=ans;
        int i=0;
        int j=even.size()-1;
        while (i<odd.size()){
            cur.next=odd.get(i++);
            cur=cur.next;
            if (j>=0) {
                cur.next = even.get(j--);
                cur=cur.next;
            }
        }
        cur.next=null;
        return ans.next;
    }
}
