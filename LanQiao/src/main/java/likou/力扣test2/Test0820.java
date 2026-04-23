package likou.力扣test2;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：Test0820
 * @Date：2025/8/20 10:35
 * @Filename：Test0820
 */
public class Test0820 {

    public static void main(String[] args) {
        /*int kthLargest = findKthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2);
        System.out.println(kthLargest);*/

        /*System.out.println(isValid("([])"));*/

        int[] ints = dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73});
        for (int i = 0; i < ints.length; i++) {
            System.out.println(ints[i]);
        }
    }


    static Deque<Integer> deque;
    static int[]ans;
    public static int[] dailyTemperatures(int[] nums) {
        ans=new int[nums.length];
        deque=new LinkedList<>();
        for (int i = 0; i < nums.length; i++) {
            while (!deque.isEmpty() && nums[i]>nums[deque.peekLast()]){
                Integer cur = deque.pollLast();
                ans[cur]=i-cur;
            }
            deque.addLast(i);
        }
        while (!deque.isEmpty()){
            ans[deque.pollLast()]=0;
        }
        return ans;
    }


    static class MinStack {

        Deque<Integer> date;
        Deque<Integer> min;
        public MinStack() {
            date=new LinkedList<>();
            min=new LinkedList<>();
        }

        public void push(int val) {
            date.addLast(val);
            if (min.size()==0){
                min.add(val);
            }else {
                min.addLast(Math.min(val,min.pollLast()));
            }
        }

        public void pop() {
            date.pollLast();
            min.pollLast();
        }

        public int top() {
            return date.peekLast();
        }

        public int getMin() {
            return min.peekLast();
        }
    }

    public static boolean isValid(String s) {
        Deque<Character> deque=new LinkedList<>();
        Map<Character,Character> map=new HashMap<>();
        map.put('(',')');
        map.put('{','}');
        map.put('[',']');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (map.containsKey(c)){
                deque.add(map.get(c));
            }else {
                if (deque.size()==0){
                    return false;
                }
                Character p = deque.peekLast();
                if (p==c){
                    deque.pollLast();
                }else {
                    return false;
                }
            }
        }
        return deque.size()==0;
    }

    public static int findKthLargest(int[] nums, int k) {
        sort2(nums);
        return nums[nums.length-k];
    }
    public static void sort2(int[]nums){
        partition(nums,0,nums.length-1);
    }

    public static void partition(int[]nums,int left,int right){
        if (left>=right){
            return;
        }
        int mid=left+(right-left)/2;
        partition(nums,left,mid);
        partition(nums,mid+1,right);
        addTwo(nums,left,mid,right);
    }

    public static void addTwo(int[]nums,int left,int mid,int right){
        int l=left;
        int r=mid+1;
        int t=0;
        int []temp=new int[right-left+1];
        while (l<=mid && r<=right){
            if (nums[l]<=nums[r]){
                temp[t++]=nums[l++];
            }else {
                temp[t++]=nums[r++];
            }
        }
        while (l<=mid){
            temp[t++]=nums[l++];
        }
        while (r<=right){
            temp[t++]=nums[r++];
        }
        for (int i = 0; i < temp.length; i++) {
            nums[i+left]=temp[i];
        }
    }

    public static void sort1(int[]nums){
        PriorityQueue<Integer> queue=new PriorityQueue<>();
        for (int i = 0; i < nums.length; i++) {
            queue.add(nums[i]);
        }
        for (int i = 0; i < nums.length; i++) {
            nums[i]=queue.poll();
        }
    }
}
