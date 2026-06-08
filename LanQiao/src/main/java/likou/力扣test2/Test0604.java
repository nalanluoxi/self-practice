package likou.力扣test2;

import likou.entity.ListNode;

/**
 * @author nalan_luoxi
 * @version 1.0
 * @email adrian0304@qq.com
 * @project self-practice
 * @package likou.力扣test2
 * @date 2026-06-04 17:34
 */
public class Test0604 {
    public static void main(String[] args) {

    }

    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists.length == 0){
            return null;
        }
        ListNode cur=lists[0];
        for (int i = 1; i < lists.length; i++) {
            cur=mergeTwo(cur,lists[i]);
        }
        return cur;
    }

    public static ListNode mergeTwo(ListNode l1,ListNode l2){
        if(l1 == null){
            return l2;
        }
        if(l2 == null){
            return l1;
        }
        ListNode ans=new ListNode();
        ListNode cur=ans;
        while (l1!=null && l2!=null){
            if (l1.val<l2.val){
                cur.next=l1;
                l1=l1.next;
            }else {
                cur.next=l2;
                l2=l2.next;
            }
            cur=cur.next;
        }

        if (l1!=null){
            cur.next=l1;
        }
        if (l2!=null){
            cur.next=l2;
        }
        return ans.next;

    }
}
