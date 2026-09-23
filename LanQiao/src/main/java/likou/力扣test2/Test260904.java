package likou.力扣test2;

import likou.entity.ListNode;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Test260904 {

    public static void main(String[] args) {

    }


    public static int lengthOfLongestSubstring(String s) {
        HashSet<Character> set = new HashSet<>();
        Deque<Character> deque = new LinkedList<>();
        int len = 0;
        for (int i = 0; i < s.length(); i++) {
            while (!set.isEmpty() && set.contains(s.charAt(i))) {
                Character c = deque.pollFirst();
                set.remove(c);
            }
            set.add(s.charAt(i));
            deque.addLast(s.charAt(i));
            len = Math.max(len, deque.size());
        }
        return len;
    }

    public static ListNode mergeKLists(ListNode[] lists) {
        try {
            if (lists == null || lists.length == 0) {
                return null;
            }
            return merge(lists, 0, lists.length - 1);
        } catch (Exception e) {
            return test2(lists);
        }
    }

    public static ListNode test2(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        PriorityQueue<ListNode> queue = new PriorityQueue<>((a, b) -> a.val - b.val);
        for (int i = 0; i < lists.length; i++) {
            if (lists[i] != null) {
                queue.add(lists[i]);
            }
        }

        ListNode ans = new ListNode();
        ListNode cur = ans;
        while (!queue.isEmpty()) {
            ListNode min = queue.poll();
            cur.next = min;
            cur = cur.next;
            if (min.next != null) {
                queue.add(min.next);
            }
        }

        return ans.next;
    }


    public static ListNode merge(ListNode[] listNodes, int l, int r) {
        if (l == r) {
            return listNodes[l];
        }
        int mid = l + (r - l) / 2;
        ListNode left = merge(listNodes, l, mid);
        ListNode right = merge(listNodes, mid + 1, r);
        return addTwo(left, right);
    }


    public static ListNode addTwo(ListNode n1, ListNode n2) {
        ListNode ans = new ListNode();
        ListNode cur = ans;
        while (n1 != null && n2 != null) {
            if (n1.val < n2.val) {
                cur.next = n1;
                n1 = n1.next;
                cur = cur.next;
            } else {
                cur.next = n2;
                n2 = n2.next;
                cur = cur.next;
            }
        }
        if (n1 != null) {
            cur.next = n1;
        }
        if (n2 != null) {
            cur.next = n2;
        }

        return ans.next;
    }


}
