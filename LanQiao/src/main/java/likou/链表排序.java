package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：链表排序
 * @Date：2025/4/16 20:53
 * @Filename：链表排序
 */
public class 链表排序 {


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
        ListNode head = new ListNode(4);
        ListNode node1 = new ListNode(2);
        ListNode node2 = new ListNode(1);
        ListNode node3 = new ListNode(3);
        head.next = node1;
        node1.next = node2;
        node2.next = node3;
        ListNode listNode = sortList(head);
        while (listNode != null) {
            System.out.println(listNode.val);
            listNode = listNode.next;
        }
    }

    public static ListNode sortList(ListNode head) {
        return sort(head, null);
    }

    public static ListNode sort(ListNode head, ListNode tail) {
        if (head == null) {
            return head;
        }
        if (head.next == tail) {
            head.next = null;
            return head;
        }
        ListNode slow = head;
        ListNode fast = head;
        while (fast != tail) {
            slow = slow.next;
            fast = fast.next;
            if (fast != tail) {
                fast = fast.next;
            }
        }
        ListNode h1 = sort(head, slow);
        ListNode h2 = sort(slow, tail);
        return merge(h1, h2);
    }

    public static ListNode merge(ListNode head1, ListNode head2) {
        ListNode anspre = new ListNode(-1);
        ListNode cur = anspre;

        while (head1 != null && head2 != null) {
            if (head1.val <= head2.val) {
                cur.next = head1;
                head1 = head1.next;
            } else {
                cur.next = head2;
                head2 = head2.next;
            }
            cur = cur.next;
        }

        if (head1 != null) {
            cur.next = head1;
        }
        if (head2 != null) {
            cur.next = head2;
        }
        return anspre.next;
    }

   /* public static ListNode sortList(ListNode head) {
        Queue<Integer> queue=new PriorityQueue<>();
        while (head!=null){
            queue.add(head.val);
            head=head.next;
        }
        ListNode ans=new ListNode(-1);
        ListNode cur=ans;
        while (!queue.isEmpty()){
            Integer poll = queue.poll();
            cur.next=new ListNode(poll);
            cur=cur.next;
        }
        return ans.next;
    }*/
}
