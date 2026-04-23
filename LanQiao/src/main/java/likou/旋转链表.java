package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：旋转链表
 * @Date：2025/6/11 9:42
 * @Filename：旋转链表
 */
public class 旋转链表 {
    public static void main(String[] args) {
        ListNode head = new ListNode(0);
        //ListNode node1 = new ListNode(1);
        //ListNode node2 = new ListNode(2);
        //ListNode node3 = new ListNode(4);
        //ListNode node4 = new ListNode(5);
        //head.next = node1;
        //node1.next = node2;
        //node2.next = node3;
        //node3.next = node4;
        ListNode listNode = rotateRight(head, 0);
        while (listNode != null) {
            System.out.println(listNode.val);
            listNode = listNode.next;
        }
    }

    public static ListNode rotateRight(ListNode head, int k) {
        // 处理空链表或单节点链表的情况
        if (head == null || head.next == null) {
            return head;
        }
        
        // 计算链表长度
        int len = 1;
        ListNode temp = head;
        while (temp.next != null) {
            len++;
            temp = temp.next;
        }
        
        // 如果k大于链表长度，取模
        k = k % len;
        if (k == 0) {
            return head;
        }
        
        ListNode fast = head;
        ListNode slow = head;
        int i = k;
        while (i > 0) {
            fast = fast.next;
            i--;
        }
        
        while (fast.next != null) {
            fast = fast.next;
            slow = slow.next;
        }

        ListNode nhead = slow.next;
        slow.next = null;
        fast.next = head;
        return nhead;
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
