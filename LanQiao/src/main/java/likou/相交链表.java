package likou;

import java.util.HashSet;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：相交链表
 * @Date：2025/3/31 16:41
 * @Filename：相交链表
 */
public class 相交链表 {
    public static void main(String[] args) {

    }


    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
            next = null;
        }
    }

  /*  public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }
        ListNode node1 = headA;
        ListNode node2 = headB;
        while (node1 != node2) {
            node1 = node1 == null ? headA : node1.next;
            node2 = node2 == null ? headB : node2.next;
        }
        return node1;
    }*/
  public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }
      HashSet<ListNode> hashSet = new HashSet<>();
      while (headA!=null){
          hashSet.add(headA);
          headA=headA.next;
      }
      while (headB!=null){
          if (hashSet.contains(headB)){
              return headB;
          }
          headB=headB.next;
      }
      return null;
    }
  }
