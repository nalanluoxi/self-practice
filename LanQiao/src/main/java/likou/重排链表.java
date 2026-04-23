package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：重排链表
 * @Date：2025/2/6 19:13
 * @Filename：重排链表
 */
public class 重排链表 {

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);

        reorderList(head);

        System.out.println("结果：");
        while (head != null) {
            System.out.println(head.val);
            head = head.next;
        }
    }


    public static void reorderList(ListNode head) {
        if (head==null){
            return;
        }
        List<ListNode>list=new ArrayList<>();
        ListNode node=head;
        while (node!=null){
            list.add(node);
            node=node.next;
        }
        int i=0,j=list.size()-1;
        while (i<j){
            list.get(i).next=list.get(j);
            i++;
            if (i==j){
                break;
            }
            list.get(j).next=list.get(i);
            j--;
        }
        list.get(i).next=null;
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
