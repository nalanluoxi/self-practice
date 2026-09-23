package likou.力扣test2;

import likou.entity.ListNode;

import java.util.List;

public class Test0805 {

    public static void main(String[] args) {
        /*ListNode n1=new ListNode(1);
        ListNode n2=new ListNode(2);
        ListNode n3=new ListNode(3);
        ListNode n4=new ListNode(4);
        ListNode n5=new ListNode(5);


        n1.next=n2;
      //  n2.next=n3;
       // n3.next=n4;
        //n4.next=n5;


        //ListNode listNode = reverse(n1, 2);
        ListNode listNode = reverseKGroup(n1, 2);
        while (listNode!=null){
            System.out.println(listNode.val);
            listNode=listNode.next;
        }*/


        ListNode n1=new ListNode(1);
        ListNode n2=new ListNode(1);
        ListNode n3=new ListNode(1);
        ListNode n4=new ListNode(2);
        ListNode n5=new ListNode(3);
        ListNode n6=new ListNode(3);
        ListNode n7=new ListNode(4);


        n1.next=n2;
        n2.next=n3;
        n3.next=n4;
        n4.next=n5;
        n5.next=n6;
        n6.next=n7;

        ListNode listNode = deleteDuplicates(n1);
        while (listNode!=null){
            System.out.println(listNode.val);
            listNode=listNode.next;
        }
    }


    public static ListNode detectCycle(ListNode head) {
        ListNode fast=head.next;
        ListNode slow=head;
        while (fast!=null && fast.next!=null){
            fast=fast.next.next;
            slow=slow.next;
            if (slow==fast){
                while (head!=slow){
                    head=head.next;
                    slow=slow.next;
                }
                return slow;
            }
        }
        return null;
    }


    public static ListNode deleteDuplicates(ListNode head) {
        ListNode ans=new ListNode();
        ans.next=head;
        ListNode cur=ans;
        while (cur!=null){
            if (cur.next!=null && cur.next.next!=null && cur.next.val==cur.next.next.val){
                int val = cur.next.val;
                while (cur.next!=null && cur.next.val==val){
                    cur.next=cur.next.next;
                }
            }else {
                cur=cur.next;
            }
        }
        return ans.next;
    }


    public static ListNode reverseKGroup(ListNode head, int k) {
        ListNode ans=new ListNode(-1);
        ans.next=head;
        ListNode fast=ans.next;
        ListNode slow=fast;
        ListNode befor=ans;
        int i=0;
        while (fast!=null){
            while (i!=k && fast!=null){
                i++;
                fast=fast.next;
            }

            if (i!=k){
                break;
            }else {
                i=0;
            }
                //fast=fast.next;
                ListNode reverse = reverse(slow, k);
                befor.next=reverse;
                befor=slow;
                slow.next=fast;
                slow=fast;

        }


        return ans.next;
    }

    public static ListNode reverse(ListNode node,int k){
        ListNode pre=null;
        ListNode cur=node;
        while (k!=0 ){
            k--;
            ListNode next = cur.next;
            cur.next=pre;
            pre=cur;
            cur=next;
        }
        return pre;
    }
}
