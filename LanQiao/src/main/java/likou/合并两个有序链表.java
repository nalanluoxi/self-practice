package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：合并两个有序链表
 * @Date：2025/1/19 10:18
 * @Filename：合并两个有序链表
 */
public class 合并两个有序链表 {
    public static void main(String[] args) {
        ListNode t1=new ListNode(1);
        t1.next=new ListNode(2);
        t1.next.next=new ListNode(4);

        ListNode t2=new ListNode(1);
        t2.next=new ListNode(3);
        t2.next.next=new ListNode(4);
        ListNode res = mergeTwoLists(t1, t2);
        while (res!=null){
            System.out.print(res.val+" ");
            res=res.next;
        }
    }

    public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode res = new ListNode();
        ListNode head=res;
        ListNode l1 = list1;
        ListNode l2 = list2;
        while (!(l1 == null && l2 == null)) {
            if (l1 == null) {
                res.next = l2;
                l2 = null;
            } else if (l2 == null) {
                res.next = l1;
                l1 = null;
            }
            else if (l1.val <= l2.val) {
                res.next = l1;
                res=res.next;
                l1 = l1.next;
            } else {
                res.next = l2;
                res=res.next;
                l2 = l2.next;
            }
        }
        return head.next;
    }

    static class ListNode {
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
