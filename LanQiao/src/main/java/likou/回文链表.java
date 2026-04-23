package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：回文链表
 * @Date：2025/2/7 10:52
 * @Filename：回文链表
 */
public class 回文链表 {
    public static void main(String[] args) {

    }

/*    public boolean isPalindrome(ListNode head) {
        List<Integer>list = new ArrayList<>();
        while (head!=null){
            list.add(head.val);
            head=head.next;
        }
        int l=0,r=list.size()-1;
        while (l<r){
            if (list.get(l)==list.get(r)){
                l++;
                r--;
            }else {
                return false;
            }
        }
        return true;
    }*/

    public boolean isPalindrome(ListNode head) {

        return true;
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
