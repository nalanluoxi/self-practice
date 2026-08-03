package likou.力扣test2;

import likou.entity.ListNode;

public class Test0803 {

    public static void main(String[] args) {
        ListNode n1=new ListNode(1);
        ListNode n2=new ListNode(1);
        ListNode n3=new ListNode(2);
        ListNode n4=new ListNode(3);
        ListNode n5=new ListNode(3);
        ListNode n6=new ListNode(4);

        n1.next=n2;
        n2.next=n3;
        n3.next=n4;
        n4.next=n5;
        n5.next=n6;

        ListNode listNode = deleteDuplicates(n1);
        while (listNode!=null){
            System.out.println(listNode.val);
            listNode=listNode.next;
        }


    }



    public static ListNode deleteDuplicates(ListNode head) {
        ListNode ans=new ListNode();
        ans.next=head;
        ListNode cur=ans;
        while (cur.next!=null && cur.next.next!=null){
            if (cur.next.val==cur.next.next.val){
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

}
