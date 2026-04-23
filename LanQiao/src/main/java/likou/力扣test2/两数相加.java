package likou.力扣test2;

import likou.entity.ListNode;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：两数相加
 * @Date：2025/7/15 11:29
 * @Filename：两数相加
 */
public class 两数相加 {
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
        ListNode n7=new ListNode(7);
        n5.next=n6;
        n6.next=n7;

        addTwoNumbers(n1,n5);

    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        Deque<ListNode>deque1=new LinkedList<>();
        Deque<ListNode>deque2=new LinkedList<>();
        Deque<ListNode>deque3=new LinkedList<>();
        while (l1!=null){
            deque1.addLast(l1);
            l1=l1.next;
        }
        while (l2!=null){
            deque2.addLast(l2);
            l2=l2.next;
        }
        int ore=0;
        while (!deque1.isEmpty() || !deque2.isEmpty()){
            int a = deque1.isEmpty() ? 0 : deque1.pollLast().val;
            int b = deque2.isEmpty() ? 0 : deque2.pollLast().val;
            int t = a + b + ore;
            deque3.addFirst(new ListNode(t%10));
            ore=t/10;
        }
        if (ore!=0){
            deque3.addFirst(new ListNode(ore));
        }
        ListNode ans=deque3.pollFirst();
        ListNode cur=ans;
        while (!deque3.isEmpty()){
            cur.next=deque3.pollFirst();
            cur=cur.next;
        }
        return ans;
    }


}
