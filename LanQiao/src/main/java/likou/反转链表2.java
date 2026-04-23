package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：反转链表2
 * @Date：2025/3/26 20:09
 * @Filename：反转链表2
 */
public class 反转链表2 {
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
        ListNode listNode = reverseBetween(node1, 2, 4);
        while (listNode != null) {
            System.out.println(listNode.val);
            listNode = listNode.next;
        }
    }

    public static ListNode reverseBetween(ListNode head, int left, int right) {
        ListNode ans=new ListNode(-1);
        ans.next=head;

        ListNode pre=ans;
        for (int i = 0; i < left - 1; i++) {
            pre=pre.next;
        }
        ListNode tpre=null;
        ListNode cur=pre.next;
        for (int i = 0; i < right - left + 1; i++) {
            ListNode next = cur.next;
            cur.next = tpre;
            tpre = cur;
            cur = next;
        }
        pre.next.next=cur;
        pre.next=tpre;
        return ans.next;
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
