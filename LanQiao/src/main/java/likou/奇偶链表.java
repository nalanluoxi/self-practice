package likou;

import likou.entity.ListNode;

import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：奇偶链表
 * @Date：2025/7/11 12:27
 * @Filename：奇偶链表
 */
public class 奇偶链表 {

    public static ListNode oddEvenList(ListNode head) {
        if (head==null||head.next==null){
            return head;
        }
        ListNode evenHead=head.next;
        ListNode odd=head,even=evenHead;
        while (even!=null && even.next!=null){
            odd.next=even.next;
            odd=odd.next;
            even.next=odd.next;
            even=even.next;
        }
        odd.next=evenHead;
        return head;
    }
}
