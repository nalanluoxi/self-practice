package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：反转链表
 * @Date：2025/2/14 10:47
 * @Filename：反转链表
 */
public class 反转链表 {
    public static void main(String[] args) {
        ListNode head =new ListNode(1);
        ListNode node1 =new ListNode(2);
        ListNode node2 =new ListNode(3);
        ListNode node3 =new ListNode(4);
        ListNode node4 =new ListNode(5);
        head.next=node1;
        node1.next=node2;
        node2.next=node3;
        node3.next=node4;
        ListNode listNode = reverseList(head);
        while (listNode!=null){
            System.out.println(listNode.val);
            listNode=listNode.next;
        }
    }

    public static ListNode reverseList(ListNode head) {
        ListNode pre = null;
        ListNode cur = head;
        while (cur != null) {
            ListNode temp = cur.next;
            cur.next=pre;
            pre=cur;
            cur=temp;
        }
        return pre;
    }


   static class ListNode {
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
