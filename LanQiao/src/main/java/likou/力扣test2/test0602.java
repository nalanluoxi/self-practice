package likou.力扣test2;

import likou.entity.ListNode;

import java.util.*;

/**
 * @author nalan_luoxi
 * @version 1.0
 * @email adrian0304@qq.com
 * @project self-practice
 * @package likou.力扣test2
 * @date 2026-06-02 17:03
 */
public class test0602 {


    public static void main(String[] args) {
        /*int[]num={3,2,1,5,6,4};
        System.out.println(findKthLargest(num,2));*/
        ListNode head=new ListNode(1);
        head.next=new ListNode(2);
        head.next.next=new ListNode(3);
        head.next.next.next=new ListNode(4);
        head.next.next.next.next=new ListNode(5);
        System.out.println(reverseKGroup(head,2).val);

    }


    public String longestPalindrome(String s) {

    }


    public static ListNode reverseKGroup(ListNode head, int k) {
        ListNode cur=head;
        ListNode cur2=head;
        ListNode ans=new ListNode();
        ListNode ans2=ans;

        int i=0;
        while (cur!=null){
            while (i!=k ){
                if (cur==null){
                    return ans2.next;
                }
                i++;
                cur=cur.next;
            }
            ListNode removeHead = remove(cur2, k);
            ans.next=removeHead;
            cur2.next=cur;
            cur2=cur;
            while (i!=0){
                i--;
                ans=ans.next;
            }
        }
        return ans2.next;
    }

    public static ListNode remove(ListNode head,int k){
        ListNode pre=null;
        ListNode cur=head;
        while (cur!=null & k!=0){
            k--;
            ListNode next = cur.next;
            cur.next=pre;
            pre=cur;
            cur=next;
        }
        return pre;
    }


    public static int findKthLargest(int[] nums, int k) {
        sort(nums,0,nums.length-1);
        return nums[nums.length-k];
    }

    public static void sort(int[]nums,int left,int right){
        if (left>=right){
            return;
        }
        int mid = left + (right - left) / 2;
        sort(nums,left,mid);
        sort(nums,mid+1,right);
        addTwo(nums,left,mid,right);
    }

    public static void addTwo(int[]nums,int left,int mid,int right){
        int len = right - left+1;
        int[]temp=new int[len];
        int i=left,j=mid+1,k=0;
        while (i<=mid && j<=right){
            if (nums[i]<=nums[j]){
                temp[k++]=nums[i++];
            }else {
                temp[k++]=nums[j++];
            }
        }
        while (i<=mid){
            temp[k++]=nums[i++];
        }
        while (j<=right){
            temp[k++]=nums[j++];
        }
        for (int l = 0; l < temp.length; l++) {
            nums[left+l]=temp[l];
        }
    }

    static class LRUCache {

        Map<Integer,Node>map;
        Node head,tail;
        int size;

        public LRUCache(int capacity) {
            size=capacity;
            map=new HashMap<>();
            head=new Node();
            tail=new Node();
            head.next=tail;
            tail.pre=head;
        }

        public int get(int key) {
            if (map.containsKey(key)){
                Node node = map.get(key);
                update(node);
                return node.value;
            }else {
                return -1;
            }
        }

        private void update(Node node){
            node.pre.next=node.next;
            node.next.pre=node.pre;

            head.next.pre=node;
            node.next=head.next;
            head.next=node;
            node.pre=head;

        }

        private void removeLast(){
            Node remove = tail.pre;
            map.remove(remove.key);
            tail.pre=remove.pre;
            remove.pre.next=tail;
        }
        public void put(int key, int value) {
            if (map.containsKey(key)){
                Node node = map.get(key);
                node.value=value;
                update(node);

                return;
            }else {
                if (map.size()==size){
                    removeLast();
                }
                Node node = new Node();
                node.key=key;
                node.value=value;
                head.next.pre=node;
                node.next=head.next;
                head.next=node;
                node.pre=head;
                map.put(key,node);
            }
        }

        class Node{
            Integer key,value;
            Node pre,next;
        }
    }




    public static int lengthOfLongestSubstring(String s) {
        Set<Character> set=new HashSet<>();
        Deque<Character> deque=new LinkedList<>();
        int maxlen=0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            while (!set.isEmpty() && set.contains(c)){
                Character remove = deque.remove();
                set.remove(remove);
            }
            set.add(c);
            deque.add(c);
            maxlen=Math.max(maxlen,deque.size());
        }
        return maxlen;
    }
}
