package example.java2;

import java.lang.reflect.Array;
import java.util.*;

public class practice {

    public static void main(String[] args) {
        /*int[] ints = dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73});
        for (int anInt : ints) {
            System.out.println(anInt);
        }*/
       /* int i = sumSubarrayMins(new int[]{3, 1, 2, 4});
        System.out.println(i);*/
       /* int i = largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3});
        System.out.println(i);*/
       /* int i = maximalRectangle(new String[]{"10100", "10111", "11111", "10010"});
        System.out.println(i);*/

       /* int i = maxWidthRamp(new int[]{9, 8, 1, 0, 1, 9, 4, 0, 4, 1});
        System.out.println(i);*/
       /* int[] ints = maxSlidingWindow(new int[]{1,-1}, 1);
        for (int anInt : ints) {
            System.out.println(anInt);
        }*/

       /* int[] ints = exclusiveTime(2,List.of("0:start:0","1:start:2","1:end:5","0:end:6"));
        for (int anInt : ints) {
            System.out.println(anInt);
        }*/
        List<List<Integer>> lists = threeSum(new int[]{-1, 0, 1, 2, -1, -4});
        for (List<Integer> list : lists) {
            for (Integer last : list) {
                System.out.print(last + ",");
            }
            System.out.println();
        }
    }

    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        Set<List<Integer>> set = new HashSet<>();
        if (nums.length < 3 || nums == null) {
            return ans;
        }
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            if (nums[i]>0){
                break;
            }
            if (i>0 && nums[i]==nums[i-1]){
                continue;
            }
            int l=i+1;
            int r=nums.length-1;
            while (l<r){
                int sum=nums[i]+nums[r]+nums[l];
                if (sum==0){
                    //ans.add(List.of(nums[i],nums[r],nums[l]));
                    while (l<r && nums[l]==nums[l+1]){
                        l++;
                    }
                    while (l<r && nums[r]==nums[r-1]){
                        r--;
                    }
                    l++;
                    r--;
                }else if (sum<0){
                    l++;
                } else if (sum>0) {
                    r--;
                }
            }
        }
        return ans;
    }
/*
    public static int[] exclusiveTime(int n, List<String> logs) {
        Deque<Integer> stack=new LinkedList<>();
        int []ans=new int[n];
        int now=-1;
        for (String log : logs) {
            String[] split = log.split(":");
            int id=Integer.parseInt(split[0]);
            String status=split[1];
            int time=Integer.parseInt(split[2]);
            if (status.equals("start")){
                if (!stack.isEmpty()){
                    Integer last = stack.peekLast();
                    ans[last]+=time-now;
                }
                stack.offerLast(id);
                now=time;
            } else if (status.equals("end")) {
                Integer last = stack.pollLast();
                ans[last]+=time-now+1;
                now=time+1;
            }
        }
        return ans;
    }
*/

 /*   public static List<Integer> largestValues(TreeNode root) {
        ArrayList<Integer> list = new ArrayList<>();
        Deque<TreeNode> deque = new LinkedList<>();
        if (root == null) {
            return list;
        }
        deque.offer(root);
        while (!deque.isEmpty()) {
            int size = deque.size();
            int tempmax=Integer.MIN_VALUE;
            while (size>0){
                TreeNode node = deque.pollLast();
                tempmax=Math.max(node.val,tempmax);
                if (node.left!=null){
                    deque.offerFirst(node.left);
                }
                if (node.right!=null){
                    deque.offerFirst(node.right);
                }
                size--;
            }
            list.add(tempmax);
        }
        return list;
    }*/


    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

  /*  static Deque<Integer> stack;
    public static int[] maxSlidingWindow(int[] nums, int k) {
        stack=new LinkedList<>();
        int []ans=new int[nums.length-k+1];
        for (int i = 0; i < k; i++) {
            while (!stack.isEmpty()&& nums[stack.peekLast()]<=nums[i]){
                stack.pollLast();
            }
            stack.offerLast(i);
        }
        ans[0]=nums[stack.peekFirst()];
        for (int i = k; i < nums.length; i++) {
            while (!stack.isEmpty()&&stack.peekFirst()<=i-k){
                stack.pollFirst();
            }
            while (!stack.isEmpty()&& nums[i]>=nums[stack.peekLast()]){
                stack.pollLast();
            }
            stack.offerLast(i);
            ans[i-k+1]=nums[stack.peekFirst()];
        }
        return ans;
    }
*/

    // static  Stack<Integer> stack;





   /* public static int maximalRectangle(String[] list) {
        if (list.length==0){
            return 0;
        }
        char[] chars = list[0].toCharArray();
        int[] arr=new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            arr[i]=chars[i]-'0';
        }
        int max = getMax(arr);
        for (int index = 1; index< list.length; index++) {
            char[] tchars = list[index].toCharArray();
            for (int i = 0; i < tchars.length; i++) {
                if (tchars[i]=='1'){
                    arr[i]+=1;
                } else if (tchars[i]=='0') {
                    arr[i]=0;
                }
            }
            max=Math.max(max,getMax(arr));
        }

        return max;
    }

    static Stack<Integer> stack;
    public static int getMax(int [] arr){
        stack=new Stack<>();
        int max=Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            while (!stack.isEmpty()&&arr[i]<=arr[stack.peek()]){
                Integer cur = stack.pop();
                int left=stack.isEmpty()?-1:stack.peek();
                int temp = arr[cur] * (i - left - 1);
                max=Math.max(max,temp);
            }
            stack.push(i);
        }
        while (!stack.isEmpty()){
            Integer cur = stack.pop();
            int left=stack.isEmpty()?-1:stack.peek();
            int temp = arr[cur] * (arr.length - left - 1);
            max=Math.max(max,temp);
        }
        return max;
    }*/
   /* static Stack<Integer> stack;
    public static int largestRectangleArea(int[] heights) {
        stack=new Stack<>();
        int max=Integer.MIN_VALUE;
        for (int i = 0; i < heights.length; i++) {
            while (!stack.isEmpty()&&heights[i]<=heights[stack.peek()]){
                Integer cur = stack.pop();
                int left=stack.isEmpty()?-1:stack.peek();
                int temp = heights[cur] * (i - left - 1);
                max=Math.max(max,temp);
            }
            stack.push(i);
        }
        while (!stack.isEmpty()){
            Integer cur = stack.pop();
            int left=stack.isEmpty()?-1:stack.peek();
            int temp = heights[cur] * (heights.length - left - 1);
            max=Math.max(max,temp);
        }

        return max;
    }*/


 /*   static Stack<Integer> stack;
    static int mod = (int) 1e9 + 7;

    public static int sumSubarrayMins(int[] arr) {
        stack = new Stack<>();
        int len = arr.length;
        long sum = 0;
        for (int i = 0; i < len; i++) {
            while (!stack.isEmpty() && arr[i] <= arr[stack.peek()]) {
                Integer cur = stack.pop();
                int left = stack.isEmpty() ? -1 : stack.peek();
                sum = (sum + ((long) (cur - left) * (i - cur) * arr[cur]) % mod) % mod;
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            Integer cur = stack.pop();
            int left = stack.isEmpty() ? -1 : stack.peek();
            sum = (sum + ((long) (cur - left) * (len - cur) * arr[cur]) % mod) % mod;
        }
        return (int) sum;
    }*/


/*
    static Stack<Integer> stack;
    static int[] ans;

    public static int[] dailyTemperatures(int[] t) {
        stack=new Stack<>();
        ans=new int[t.length];
        for (int i = 0; i < t.length; i++) {
            while (!stack.isEmpty()&&t[i]>t[stack.peek()]){
                Integer cur = stack.pop();
                ans[cur]=i-cur;
            }
            stack.push(i);
        }
        while (!stack.isEmpty()){
            ans[stack.pop()]=0;
        }
        return ans;
    }*/
}
