package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：合并k哥升序链表
 * @Date：2025/3/31 11:18
 * @Filename：合并k哥升序链表
 */
public class 合并k哥升序链表 {
    public static void main(String[] args) {
        /*ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(4);
        ListNode node3 = new ListNode(5);
        ListNode node4 = new ListNode(1);
        ListNode node5 = new ListNode(3);
        ListNode node6 = new ListNode(4);
        ListNode node7 = new ListNode(2);
        ListNode node8 = new ListNode(6);
        node1.next = node2;
        node2.next = node3;
        node4.next = node5;
        node5.next = node6;
        node7.next = node8;
        ListNode[] lists=new ListNode[3];
        lists[0]=node1;
        lists[1]=node4;
        lists[2]=node7;*/
        ListNode node1=new ListNode(-2);
        ListNode node2=new ListNode(-3);
        ListNode node3=new ListNode(-2);
        ListNode node4=new ListNode(1);
        node2.next=node3;
        node3.next=node4;
        ListNode[] lists=new ListNode[3];
        lists[1]=node1;
        lists[2]=node2;
        //ListNode listNode = andTowLists(lists[0], lists[1]);
        ListNode listNode = mergeKLists(lists);
        while (listNode!= null) {
            System.out.println(listNode.val);
            listNode = listNode.next;
        }
    }

    public static ListNode mergeKLists(ListNode[] lists) {
        ListNode ans=new ListNode(Integer.MIN_VALUE);
        for (int i = 0; i < lists.length; i++) {
            ans=andTowLists(ans,lists[i]);
        }
        return ans.next;
    }


    public static ListNode andTowLists(ListNode listNode1,ListNode listNode2){
        ListNode cur=new ListNode(Integer.MIN_VALUE);
        ListNode now=cur;
        while (listNode1!=null&&listNode2!=null){
            if (listNode1.val<=listNode2.val){
                cur.next=listNode1;
                listNode1=listNode1.next;
            }else {
                cur.next=listNode2;
                listNode2=listNode2.next;
            }
            cur=cur.next;
        }

        if (listNode1!=null){
            cur.next=listNode1;
        }
        if (listNode2!=null){
            cur.next=listNode2;
        }
        return now.next;
    }
   /* public static ListNode mergeKLists(ListNode[] lists) {
        ListNode now=new ListNode(-1);
        ListNode cur=now;
       while (true){
           int tempmin = getmin(lists);
           if (tempmin==Integer.MAX_VALUE){
               break;
           }
           cur.next=new ListNode(tempmin);
           cur=cur.next;
       }
        return now.next;
    }
    public static int getmin(ListNode[] lists){
        int min=Integer.MAX_VALUE;
        int index=-1;
        for (int i = 0; i < lists.length; i++) {
            if (lists[i]==null)continue;
            if (lists[i].val<=min){
                min=lists[i].val;
                index=i;
            }
        }
        if (index==-1){
            return Integer.MAX_VALUE;
        }
        lists[index]=lists[index].next;
        return min;
    }
*/

    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

}
