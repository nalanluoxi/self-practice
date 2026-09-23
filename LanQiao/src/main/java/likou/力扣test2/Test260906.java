package likou.力扣test2;

import likou.entity.ListNode;

public class Test260906 {
    public static void main(String[] args) {
        ListNode n1 = new ListNode(1);
        ListNode n2 = new ListNode(2);
        ListNode n3 = new ListNode(3);
        ListNode n4 = new ListNode(4);
        ListNode n5 = new ListNode(5);
        ListNode n6 = new ListNode(6);
        n1.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = n5;
       // n5.next = n6;

        ListNode listNode = reverseKGroup(n1,4);
        while (listNode!=null){
            System.out.println(listNode.val);
            listNode=listNode.next;
        }
    }

    //k=3
    //ans- 1 - 2 - 3 - 4 - 5
    // t              b c
    // t   2 1 3 4 5
    public static ListNode reverseKGroup(ListNode head, int k) {
        ListNode ans=new ListNode(-1,head);
        ListNode t=ans;
        ListNode cur=ans.next;
        ListNode befor=cur;

        int i=k;
        while (cur!=null){
            i--;
            cur=cur.next;
            if (i==0){
                ListNode reverseHead = reverseNums(befor, k);
                befor.next=cur;
                ans.next=reverseHead;
                ans=befor;
                befor=befor.next;
                i=k;
            }
        }
        return t.next;
    }

    public static ListNode reverseNums(ListNode head, int k) {
        ListNode pre = null;
        ListNode cur = head;

        int i = 0;
        while (i != k) {
            i++;
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }


    public static int findKthLargest(int[] nums, int k) {
        sort(nums, 0, nums.length - 1);
        return nums[nums.length - k];
    }

    public static void sort(int[] nums, int left, int right) {
        if (left >= right) {
            return;
        }
        int mid = left + (right - left) / 2;
        sort(nums, left, mid);
        sort(nums, mid + 1, right);

        addTwo(nums, left, mid, right);
    }


    public static void addTwo(int[] nums, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;
        while (i <= mid && j <= right) {
            if (nums[i] <= nums[j]) {
                temp[k++] = nums[i++];
            } else {
                temp[k++] = nums[j++];
            }
        }
        while (i <= mid) {
            temp[k++] = nums[i++];
        }
        while (j <= right) {
            temp[k++] = nums[j++];
        }
        for (int l = 0; l < temp.length; l++) {
            nums[left + l] = temp[l];
        }


    }
}
