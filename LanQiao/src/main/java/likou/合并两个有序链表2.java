package likou;

import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou.面试
 * @Project：LanQiaoBei
 * @name：合并两个有序链表
 * @Date：2025/3/21 9:23
 * @Filename：合并两个有序链表
 */
public class 合并两个有序链表2 {
    public static void main(String[] args) {
        ListNode l1=new ListNode(1,new ListNode(2,new ListNode(4,new ListNode(9))));
        ListNode l2=new ListNode(1,new ListNode(3,new ListNode(4,new ListNode(6,new ListNode(10)))));

        ListNode listNode = mergeTwoLists(l1, l2);
        while (listNode!=null){
            System.out.println(listNode.val);
            listNode=listNode.next;
        }
    }

    public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode ans=new ListNode();
        ListNode pre=ans;
        while (list1!=null && list2!=null){
           if (list1.val <= list2.val){
               ans.next=list1;
               list1=list1.next;
               ans=ans.next;
           }else {
               ans.next=list2;
               list2=list2.next;
               ans=ans.next;
           }
        }
        if (list1!=null){
            ans.next=list1;
        }
        if (list2!=null){
            ans.next=list2;
        }
        return pre.next;
    }


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

}
