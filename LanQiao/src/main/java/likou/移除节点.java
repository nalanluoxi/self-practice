package likou;

import java.util.ArrayDeque;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：移除节点
 * @Date：2025/2/8 12:05
 * @Filename：移除节点
 */
public class 移除节点 {

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(1);
        ListNode node3 = new ListNode(1);
        // ListNode node4 = new ListNode(8);
        head.next = node1;
        node1.next = node2;
        node2.next = node3;
        //node3.next = node4;
        ListNode listNode = removeNodes(head);
        while (listNode != null) {
            System.out.println(listNode.val);
            listNode = listNode.next;
        }
    }

    public static ListNode removeNodes(ListNode head) {
        ArrayDeque<ListNode> stack = new ArrayDeque<>();
        ListNode cur = head;
        while (cur != null) {
            while (!stack.isEmpty() && stack.peekLast().val < cur.val) {
                stack.removeLast();
            }
            stack.addLast(cur);
            cur = cur.next;
        }
        ListNode ans = new ListNode();
        cur=ans;
        while (!stack.isEmpty()) {
            cur.next = stack.removeFirst();
            cur = cur.next;
        }
        return ans.next;
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
