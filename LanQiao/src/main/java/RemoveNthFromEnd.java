public class RemoveNthFromEnd {
    public static void main(String[] args) {

    }

    public static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode pre=new ListNode(-1,head);
        ListNode fast=pre;
        ListNode slow=pre;

        for (int i = 0; i <= n; i++) {
            fast=fast.next;
        }

        while (n!=1&&fast!=null){
            fast=fast.next;
            slow=slow.next;
        }

        slow.next=slow.next.next;
        return pre.next;

    }

    public static ListNode swapPairs(ListNode head) {

        if (head==null||head.next==null){
            return  head;
        }
        ListNode dhead=new ListNode(-1,head);
        ListNode pre=dhead;
        ListNode cur=head;

        while (cur.next!=null){
            pre.next=cur.next;
            cur.next=cur;
            cur.next=cur.next.next;
            pre=pre.next.next;
            cur=cur.next.next;
        }

        return dhead.next;
    }

}
