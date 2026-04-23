package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：删除排序链表中的重复元素
 * @Date：2025/5/17 22:32
 * @Filename：删除排序链表中的重复元素
 */
public class 删除排序链表中的重复元素 {
    public static void main(String[] args) {

    }

    public static ListNode deleteDuplicates(ListNode head) {
        if (head==null||head.next==null){
            return head;
        }
        ListNode cur =head;
        while (cur.next!=null){
            if (cur.val== cur.next.val){
                cur.next=cur.next.next;
            }else {
                cur=cur.next;
            }
        }
        return head;
    }



     public static class ListNode {
         int val;
         ListNode next;
         ListNode() {}
         ListNode(int val) { this.val = val; }
         ListNode(int val, ListNode next) { this.val = val; this.next = next; }
     }

}
