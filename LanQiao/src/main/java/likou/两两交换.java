package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：两两交换
 * @Date：2025/2/14 11:33
 * @Filename：两两交换
 */
public class 两两交换 {
    public static void main(String[] args) {
        ListNode head=new ListNode(1);
        ListNode head1=new ListNode(2);
        ListNode head2=new ListNode(3);
        ListNode head3=new ListNode(4);
        head.next=head1;
        head1.next=head2;
        head2.next=head3;
        ListNode listNode = swapPairs(head);
        while (listNode!=null){
            System.out.println(listNode.val);
            listNode=listNode.next;
        }
    }

    public static ListNode swapPairs(ListNode head) {
        if (head==null||head.next==null){
            return head;
        }
        ListNode pre=new ListNode();
        ListNode ans=pre;
        pre.next=head;
        ListNode cur=pre.next;
        ListNode next=cur.next;
        ListNode temp=null;
        while (true){
            if (cur==null||cur.next==null){
                break;
            }
           // System.out.println("cur:"+cur.val+" next:"+next.val);
            pre.next=cur.next;
            temp=cur.next.next;
            cur.next.next=cur;
            cur.next=temp;
            pre=cur;
            cur=cur.next;

        }
        return ans.next;
    }


    static public class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

}
