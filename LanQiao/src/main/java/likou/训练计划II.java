package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：训练计划II
 * @Date：2025/4/27 9:04
 * @Filename：训练计划II
 */
public class 训练计划II {
    public static void main(String[] args) {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        //node1.next = node2;
        //node2.next = node3;
        //node3.next = node4;
       // node4.next = node5;
        ListNode ans = trainingPlan(node1, 1);
        System.out.println(ans.val);
    }


    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }


    public static ListNode trainingPlan(ListNode head, int cnt) {
        ListNode temp = new ListNode(0,head);
        ListNode ans = temp;
        while (cnt>=0 && temp!=null){
            temp = temp.next;
            cnt--;
        }
        while (temp!=null){
            temp = temp.next;
            ans = ans.next;
        }
        return ans.next;
    }
}
