package likou;

import java.util.ArrayList;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：重排链表2
 * @Date：2025/3/31 16:50
 * @Filename：重排链表2
 */
public class 重排链表2 {
    public static void main(String[] args) {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        reorderList(node1);
        while (node1 != null) {
            System.out.println(node1.val);
            node1 = node1.next;
        }
    }

    public static void reorderList(ListNode head) {
        if (head == null || head.next == null) {
            return;
        }
        ListNode p1=head;
        ArrayList<ListNode> list = new ArrayList<>();
        while (p1!=null){
            list.add(p1);
            p1=p1.next;
        }
        p1=head;
        int l=1;
        int r=list.size()-1;
        while (true){
            if (l>r){
                p1.next=null;
                break;
            }
            p1.next=new ListNode(list.get(r--).val);
            p1=p1.next;
            if (l>r){
                break;
            }
            p1.next=new ListNode(list.get(l++).val);
            p1=p1.next;
        }
    }



    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

}
