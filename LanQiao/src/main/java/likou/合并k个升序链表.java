package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：合并k个升序链表
 * @Date：2025/1/19 10:43
 * @Filename：合并k个升序链表
 */
public class 合并k个升序链表 {
    public static void main(String[] args) {
        ListNode t1 = new ListNode(1);
        t1.next = new ListNode(4);
        t1.next.next = new ListNode(5);

        ListNode t2 = new ListNode(1);
        t2.next = new ListNode(3);
        t2.next.next = new ListNode(4);

        ListNode t3 = new ListNode(2);
        t3.next = new ListNode(6);

        ListNode listNode = mergeKLists(new ListNode[]{t1, t2, t3});
        while (listNode != null) {
            System.out.print(listNode.val + " ");
            listNode = listNode.next;
        }
    }

    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists.length == 0) {
            return null;
        } else if (lists.length == 1) {
            return lists[0];
        }
        ListNode res = new ListNode();
        ListNode head = res;

        int minIndex = 0;
        while (true) {
            ListNode minNode = new ListNode(Integer.MAX_VALUE);
            for (int i = 0; i < lists.length; i++) {
                ListNode temp = lists[i];
                if (temp == null) {
                    continue;
                }
                if (temp.val <= minNode.val) {
                    minNode = temp;
                    minIndex = i;
                }
            }
            if (minNode.val == Integer.MAX_VALUE){
                break;
            }
           // System.out.println("minNode[" + minIndex + "].val:" + minNode.val);
            res.next = minNode;
            lists[minIndex] = minNode.next;
          /*  if (lists[minIndex] != null) {
                System.out.println("now  list[" + minIndex + "].val :" + lists[minIndex].val);
            }*/
            res = res.next;
           // System.out.println("res.val:" + res.val);
           // System.out.println("----------------------------");
        }
        return head.next;
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
