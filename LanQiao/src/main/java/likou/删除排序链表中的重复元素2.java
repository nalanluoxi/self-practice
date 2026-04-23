package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：删除排序链表中的重复元素2
 * @Date：2025/4/16 14:19
 * @Filename：删除排序链表中的重复元素2
 */
public class 删除排序链表中的重复元素2 {

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

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode node1 = new ListNode(2);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        head.next = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        ListNode listNode = deleteDuplicates(head);
        while (listNode != null) {
            System.out.println(listNode.val);
            listNode = listNode.next;
        }
    }

    public static ListNode deleteDuplicates(ListNode head) {
        if (head==null){
            return head;
        }
        ListNode ans=new ListNode(-1,head);
        ListNode cur=ans;
        while (cur.next!=null&& cur.next.next!=null){
            if (cur.next.val==cur.next.next.val){
                int x=cur.next.val;
                while (cur.next!=null && cur.next.val==x){
                    cur.next=cur.next.next;
                }
            }else {
                cur=cur.next;
            }
        }
        return ans.next;
    }


   /* public static ListNode deleteDuplicates(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode fast = head.next;
        ListNode slow = head;
        ListNode pre = new ListNode(-1);
        ListNode ans = pre;
        while (fast != null && slow != null) {
            if (fast.val != slow.val) {
                pre.next = new ListNode(slow.val);
                pre = pre.next;
                slow = fast;
                fast = fast.next;
            } else {
                while (fast != null && fast.val == slow.val) {
                    fast = fast.next;
                }
                slow = fast;
                if (fast!=null){
                    fast=fast.next;
                }
            }
        }
        if (slow!=null){
            pre.next=new ListNode(slow.val);
        }
        return ans.next;
    }*/
}
