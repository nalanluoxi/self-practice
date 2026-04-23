package likou.力扣test2;

import likou.entity.ListNode;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：Test1026
 * @Date：2025/10/26 16:28
 * @Filename：Test1026
 */
public class Test1026 {
    public static void main(String[] args) {
        //System.out.println(lengthOfLongestSubstring("abcabcbb"));

        /*ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        ListNode listNode = reverseKGroup(node1,2);
        while (listNode!=null){
            System.out.println(listNode.val);
            listNode=listNode.next;
        }*/
        //System.out.println(findKthLargest(new int[]{3,2,1,5,6,4},2));

        /*List<List<Integer>> lists = threeSum(new int[]{-100, -70, -60, 110, 120, 130, 160});
        for (List<Integer> list : lists) {
            System.out.println(list);
        }*/
        System.out.println(maxSubArray(new int[]{-2,1,-3,4,-1,2,1,-5,4}));
    }


    public int[] sortArray(int[] nums) {
        sort(nums);
        return nums;
    }

    public static void sort(int[]nums){
        sort(nums,0,nums.length-1);
    }

    public static void sort(int[]nums,int left,int right){
        if (left>=right){
            return ;
        }
        int mid = left + (right - left) / 2;
        sort(nums,left,mid);
        sort(nums,mid+1,right);
        addTwo(nums,left,mid,right);
    }
    public static void addTwo(int []nums,int left,int mid,int right){
        int []temp=new int[right-left+1];
        for (int i = 0; i < temp.length; i++) {
            temp[i]=nums[left+i];
        }
        int i=0,j=mid-left+1;
        int k=left;
        while (i<=mid-left && j<=right-left){
            if (temp[i]<=temp[j]){
                nums[k++]=temp[i++];
            }else {
                nums[k++]=temp[j++];
            }
        }
        while (i<=mid-left){
            nums[k++]=temp[i++];
        }
        while (j<=right-left){
            nums[k++]=temp[j++];
        }
        return;
    }
    public static int maxSubArray(int[] nums) {

        int ans=nums[0];
        int sum=0;
        for (int i = 0; i < nums.length; i++) {
            sum=Math.max(nums[i],sum+nums[i]);
            ans=Math.max(ans,sum);
        }
        return ans;
    }
    public static int maxSubArray2(int[] nums) {

        int[]dp=new int[nums.length];
        dp[nums.length-1]=nums[nums.length-1];
        int max=dp[nums.length-1];
        for (int i = nums.length-2; i >= 0; i--) {
            dp[i]=Math.max(nums[i],dp[i+1]+nums[i]);
            max=Math.max(max,dp[i]);
        }
        return max;
    }

    public static List<List<Integer>> threeSum(int[] nums) {
        if (nums.length<3){
            return null;
        }
        List<List<Integer>> ans=new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
           if (nums[i]>0){
               break;
           }
           if (i!=0 && nums[i]==nums[i-1]){
               continue;
           }
           int left =i+1;
           int right =nums.length-1;
           while (left<right){
               int sum = nums[i]+nums[left]+nums[right];
               if (sum==0){
                   ans.add(List.of(nums[i],nums[left],nums[right]));
                   while (left<right && nums[left]==nums[left+1]){
                       left++;
                   }
                   while (right>left && nums[right]==nums[right-1]){
                       right--;
                   }
                   left++;
                   right--;
               } else if (sum < 0) {
                   left++;
               }else {
                   right--;
               }
           }
        }

        return ans;
    }

    public static ListNode reverseKGroup(ListNode head, int k) {
        List<ListNode> list=new ArrayList<>();
        while (head!=null){
            list.add(head);
            head=head.next;
        }
        ListNode ans=new ListNode(0);
        ListNode pre =ans;
        for (int i = 0; i < list.size(); i++) {
            if (i+k<=list.size()){
                int t = i + k-1;
                while (t>=i){
                    ans.next =new ListNode(list.get(t--).val);
                    ans=ans.next;
                }
                i+=k-1;
            }else {
                ans.next = list.get(i);
                break;
            }
        }
        return pre.next;
    }
    public static int findKthLargest(int[] nums, int k) {
        //Arrays.sort(nums);
        sort(nums);
        return nums[nums.length-k];
    }

    public static int lengthOfLongestSubstring(String s) {
        Set<Character> set = new HashSet<>();
        Deque<Character>deque=new LinkedList<>();
        int ans=0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            while (!set.isEmpty()&&set.contains(c)){
                ans=Math.max(ans,deque.size());
                set.remove(deque.removeFirst());
            }
            set.add(c);
            deque.addLast(c);
        }
        if (!deque.isEmpty()){
            ans=Math.max(deque.size(),ans);
        }
        return ans;
    }


    public static ListNode reverseList(ListNode head) {
        ListNode cur=head;
        ListNode pre =null;
        while (cur!=null){
            ListNode next = cur.next;
            cur.next=pre;
            pre=cur;
            cur=next;
        }
        return pre;
    }
}
