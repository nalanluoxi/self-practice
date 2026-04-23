package likou.力扣test2;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：Test0928
 * @Date：2025/9/28 22:39
 * @Filename：Test0928
 */
public class Test0928 {

    public static void main(String[] args) {
        /*Queue<Integer> queue=new PriorityQueue<>((o1, o2)-> o2-o1);
        queue.add(3);
        queue.add(2);
        queue.add(1);
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());*/
     /*   MedianFinder medianFinder=new MedianFinder();
        medianFinder.addNum(1);
        medianFinder.addNum(2);
        System.out.println(medianFinder.findMedian());
        medianFinder.addNum(3);
        System.out.println(medianFinder.findMedian());*/


     /*   MinStack minStack=new MinStack();
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        System.out.println(minStack.getMin());
        minStack.pop();
        System.out.println(minStack.top());
        System.out.println(minStack.getMin());*/

        System.out.println(largestRectangleArea2(new int[]{2,1,5,6,2,3}));
    }

    static Deque<Integer> deque;

    public static int largestRectangleArea(int[] nums) {
        int ans = Integer.MIN_VALUE;
        deque = new LinkedList<>();
        for (int i = 0; i < nums.length; i++) {
            while (!deque.isEmpty() && nums[i] <= nums[deque.peekLast()]) {
                Integer mid = deque.pollLast();
                int left=deque.isEmpty()?-1:deque.peekLast();
                ans = Math.max(ans, nums[mid] * (i - left - 1));
            }
            deque.offerLast(i);
        }
        while (!deque.isEmpty()) {
            Integer mid = deque.pollLast();
            int left=deque.isEmpty()?-1:deque.peekLast();
            ans = Math.max(ans, nums[mid] * (nums.length - left - 1));
        }
        return ans;
    }


    public static int largestRectangleArea2(int[] heights) {
        int max = Integer.MIN_VALUE;
        Deque<Integer> stack = new LinkedList<>();
        for (int i = 0; i < heights.length; i++) {
            while (!stack.isEmpty() && heights[i] <= heights[stack.peekLast()]) {
                Integer mid = stack.pollLast();
                int left = stack.isEmpty() ? -1 : stack.peekLast();
                max = Math.max(max, heights[mid] * (i - left - 1));
            }
            stack.addLast(i);
        }
        while (!stack.isEmpty()) {
            Integer mid = stack.pollLast();
            int left = stack.isEmpty() ? -1 : stack.peekLast();
            max = Math.max(max, heights[mid] * (heights.length - left - 1));
        }
        return max;
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
                min.addLast(val);
            }else {
                min.addLast(Math.min(val,min.peekLast()));
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
        Map<Character, Character> map = Map.of('(', ')', '{', '}', '[', ']');
        Deque<Character>deque=new LinkedList<>();
        for (int i = 0; i < s.length(); i++) {
            if (map.containsKey(s.charAt(i))){
                deque.offerLast(map.get(s.charAt(i)));
            }else {
                if (!deque.isEmpty()&&deque.peekLast()==s.charAt(i)){
                    deque.pollLast();
                }else {
                    return false;
                }
            }
        }
        return deque.size()==0;
    }

    static class MedianFinder {

        Queue<Integer> maxToMin;
        Queue<Integer> minToMax;
        public MedianFinder() {
            maxToMin = new PriorityQueue<>();//大的一半
            minToMax=new PriorityQueue<>((o1, o2) -> o2-o1);//小的一半
        }

        public void addNum(int num) {
            if (maxToMin.size()==minToMax.size()){
                maxToMin.add(num);
                minToMax.add(maxToMin.poll());
            }else {
                minToMax.add(num);
                maxToMin.add(minToMax.poll());
            }
        }

        public double findMedian() {
            if (minToMax.size()==maxToMin.size()){
                return (minToMax.peek()+maxToMin.peek())/2.0;
            }else {
                return minToMax.peek();
            }
        }
    }
}
