package luogu;

import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：luogu
 * @Project：LanQiaoBei
 * @name：K个一组反转链表
 * @Date：2025/3/19 10:28
 * @Filename：K个一组反转链表
 */
public class K个一组反转链表 {
    public static void main(String[] args) {
        ListNode head=new ListNode(1);
        ListNode head1=new ListNode(2);
        /*ListNode head2=new ListNode(3);
        ListNode head3=new ListNode(4);
        ListNode head4=new ListNode(5);*/
        head.next=head1;
       /* head1.next=head2;
        head2.next=head3;
        head3.next=head4;*/
        ListNode listNode = reverseKGroup(head, 2);
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


    public static ListNode reverseKGroup(ListNode head, int k) {
        ListNode cur = head;
        ListNode now=head;
        ListNode ans=new ListNode();
        ListNode pre=ans;
        pre.next=head;
        int count =0;
        while (cur !=null){
            count++;
            cur=cur.next;
            if (count==k){
                ListNode reverse = reverse(now, k);
                ans.next=reverse;
                while (count!=0){
                    count--;
                    ans=ans.next;
                }
                now.next=cur;
                now=now.next;
                continue;
            }
        }
        return pre.next;
    }

    public static ListNode reverse(ListNode heade,int k){
        ListNode cur=heade;
        ListNode pre=null;
        ListNode tempnext=null;
        while (cur!=null && k!=0){
            tempnext = cur.next;
            cur.next=pre;
            pre=cur;
            cur=tempnext;
            k--;
        }
        return pre;
    }
}
