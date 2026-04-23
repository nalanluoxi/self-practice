package example.java2;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2
 * @Project：LanQiaoBei
 * @name：反转链表
 * @Date：2025/3/16 10:36
 * @Filename：反转链表
 */
public class 反转链表 {
    public static void main(String[] args) {
        ListNode l1= new ListNode(1);
        System.out.println(l1);
        ListNode l2= new ListNode(2);
        ListNode l3= new ListNode(3);
        ListNode l4= new ListNode(4);
        l1.next=l2;
        l2.next=l3;
        l3.next=l4;
        l4.next=null;
        ListNode listNode = reverseList(l1);
        while (listNode!=null){
            System.out.println(listNode.val);
            listNode=listNode.next;
        }
    }

    public static class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 }


/*    public static ListNode reverseList(ListNode head) {
        ListNode pre = null;
        ListNode cur = head;
        while (cur != null) {
            ListNode temp = cur.next;
            cur.next=pre;
            pre=cur;
            cur=temp;
        }
        return pre;
    }*/
    public static ListNode reverseList(ListNode head) {
        ListNode pre=new ListNode();
        ListNode now=head;
        while (now!=null){
            System.out.println(now.val);
            ListNode next=now.next;
            System.out.println(next.val);
            next.next=pre;
            pre=now;
            now=next;
        }
        return pre;
    }
}
