public class RemoveElements {


    public static void main(String[] args) {
        ListNode nextnextnode=new ListNode(1);
        ListNode nextnode=new ListNode(2,nextnextnode);
        ListNode head=new ListNode(6,nextnode);
        removeElements(head,2);
    }
    public static ListNode removeElements(ListNode head, int val) {
        if (head==null){
            return head;
        }

        ListNode dhead=new ListNode(-1,head);
        ListNode pre=dhead;
        ListNode now=head;

        while (now!=null){
            if (now.val==val){
                pre.next=now.next;
            }else {
                pre=now;
            }
            now=now.next;
        }
        return dhead.next;
    }


}
