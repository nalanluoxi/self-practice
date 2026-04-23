package likou;

import javax.print.DocFlavor;
import java.awt.image.ImageProducer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：环形链表2
 * @Date：2025/4/3 16:13
 * @Filename：环形链表2
 */
public class 环形链表2 {

    public static void main(String[] args) {
        ListNode head=new ListNode(1);
        ListNode node1=new ListNode(2);
        ListNode node2=new ListNode(3);
        ListNode node3=new ListNode(4);
        head.next=node1;
        node1.next=node2;
        node2.next=node3;
        node3.next=head;
        ListNode listNode = detectCycle(head);
        System.out.println(listNode.val);
    }

    public static ListNode detectCycle(ListNode head) {
        ListNode slow=head;
        ListNode fast=head;
        while (fast!=null&&fast.next!=null){
            fast=fast.next.next;
            slow=slow.next;
            if (fast==slow){
                while (head!=slow){
                    head=head.next;
                    slow=slow.next;
                }
                return slow;
            }
        }
        return null;
    }
/*    static HashSet<ListNode> set;
    public static ListNode detectCycle(ListNode head) {
        set=new HashSet<>();
        ListNode cur=head;
        while (cur!=null){
            if (set.contains(cur)){
                return cur;
            }
            set.add(cur);
            cur=cur.next;
        }
        return null;
    }*/



    static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {
            val = x;
            next = null;
        }
    }

}
