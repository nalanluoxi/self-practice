package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：环形链表
 * @Date：2025/3/26 19:50
 * @Filename：环形链表
 */
public class 环形链表 {
    public static void main(String[] args) {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        //node4.next=node2;
        boolean b = hasCycle(node1);
        System.out.println(b);
    }

    public static boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        ListNode fast = head.next;
        ListNode slow = head;
        int v=5;
        int count = v;
        while (fast != slow) {
            if (fast.next == null || slow == null) {
                return false;
            }
            while (fast.next != null && count > 0) {
                fast = fast.next;
                count--;
            }
            slow = slow.next;
            count = v;
        }
        return true;
    }


    static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
            next = null;
        }
    }

}
