package likou.entity;

/**
 * @Author 纳兰洛熙
 * @Package：likou.entity
 * @Project：LanQiaoBei
 * @name：ListNode
 * @Date：2025/7/8 9:39
 * @Filename：ListNode
 */
public class ListNode {
   public int val;
   public ListNode next;
   public ListNode() {}
   public ListNode(int val) { this.val = val; }
   public ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}
