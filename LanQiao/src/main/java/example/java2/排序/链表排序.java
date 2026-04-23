package example.java2.排序;

import java.util.*;
import java.util.stream.Collector;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2.排序
 * @Project：LanQiaoBei
 * @name：链表排序
 * @Date：2025/6/9 10:51
 * @Filename：链表排序
 */
public class 链表排序 {

    public static void main(String[] args) {
        ListNode listNode=new ListNode(4);
        listNode.next=new ListNode(2);
        listNode.next.next=new ListNode(1);
        listNode.next.next.next=new ListNode(3);
        ListNode listNode1 = sortList(listNode);
        while (listNode1!=null){
            System.out.println(listNode1.val);
            listNode1=listNode1.next;
        }
    }
    static List<Integer> list;
    public static ListNode sortList(ListNode head) {
        list=new ArrayList<>();
        ListNode cur=head;
        while(cur!=null){
            list.add(cur.val);
            cur=cur.next;
        }
        Collections.sort(list);
        ListNode ans=new ListNode();
        cur=ans;
        for (Integer i : list) {
            cur.next = new ListNode(i);
            cur=cur.next;
        }
        cur.next=null;
        return ans.next;
    }





   public static class ListNode {
       int val;
       ListNode next;
       ListNode() {}
       ListNode(int val) { this.val = val; }
       ListNode(int val, ListNode next) { this.val = val; this.next = next; }
   }

}
