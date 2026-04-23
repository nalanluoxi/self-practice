package likou;

import likou.entity.ListNode;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Locale;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：两数相加2
 * @Date：2025/7/9 9:42
 * @Filename：两数相加2
 */
public class 两数相加2 {

    public static void main(String[] args) {
        ListNode n1=new ListNode(7);
        ListNode n2=new ListNode(2);
        ListNode n3=new ListNode(4);
        ListNode n4=new ListNode(3);

        n1.next=n2;
        n2.next=n3;
        n3.next=n4;

        ListNode n5=new ListNode(5);
        ListNode n6=new ListNode(6);
        ListNode n7=new ListNode(4);

        n5.next=n6;
        n6.next=n7;

        ListNode listNode = addTwoNumbers(n1, n5);
        while (listNode!=null){
            System.out.println(listNode.val);
            listNode=listNode.next;
        }
    }
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        Deque<ListNode> deque1=new LinkedList<>();
        Deque<ListNode> deque2=new LinkedList<>();
        ListNode c1=l1;
        ListNode c2=l2;
        while (c1!=null){
            deque1.offerLast(c1);
            c1=c1.next;
        }
        while (c2!=null){
            deque2.offerLast(c2);
            c2=c2.next;
        }
        int n=0;
        Deque<ListNode> ansdeque=new LinkedList<>();
        while (!deque1.isEmpty()||!deque2.isEmpty()){
            int n1= deque1.isEmpty()? 0:deque1.pollLast().val;
            int n2= deque2.isEmpty()?0:deque2.pollLast().val;
            int t = n + n1 + n2;
            ansdeque.add(new ListNode(t%10));
            n=t/10;
        }
        if (n!=0){
            ansdeque.add(new ListNode(n));
        }
        ListNode ans=new ListNode();
        ListNode cur=ans;
        while (!ansdeque.isEmpty()){
            cur.next=ansdeque.pollLast();
            cur=cur.next;
        }
        return ans.next;
    }
}
