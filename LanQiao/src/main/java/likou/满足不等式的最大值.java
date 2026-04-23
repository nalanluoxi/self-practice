package likou;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：满足不等式的最大值2
 * @Date：2025/3/7 16:17
 * @Filename：满足不等式的最大值2
 */
public class 满足不等式的最大值 {
    public static void main(String[] args) {
        int maxValueOfEquation = findMaxValueOfEquation(new int[][]{
                {-19,-12},{-13,-18},{-12,18},{-11,-8},
                {-8,2},{-7,12},{-5,16},{-3,9},{1,-7},
                {5,-4},{6,-20},{10,4},{16,4},{19,-9},
                {20,19}}, 6);
        System.out.println(maxValueOfEquation);
    }

    public  static Deque<Integer> deque;
    public static int findMaxValueOfEquation(int[][] points, int k) {
        deque=new LinkedList<>();
        int ans=Integer.MIN_VALUE;
        for (int index = 0; index < points.length; index++) {
            int x = points[index][0];
            int y = points[index][1];
            int temp = y - x;
            while (!deque.isEmpty()&&points[deque.peekFirst()][0]+k<x){
                deque.removeFirst();
            }
            if (!deque.isEmpty()){
                ans=Math.max(ans,x+y+points[deque.peekFirst()][1]-points[deque.peekFirst()][0]);
            }

            while (!deque.isEmpty() && points[deque.peekLast()][1]-points[deque.peekLast()][0]<=temp ){
                deque.pollLast();
            }
            deque.offerLast(index);
        }
        return ans;
    }
}
