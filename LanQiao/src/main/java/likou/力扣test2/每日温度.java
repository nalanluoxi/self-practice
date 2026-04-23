package likou.力扣test2;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：每日温度
 * @Date：2025/5/28 22:22
 * @Filename：每日温度
 */
public class 每日温度 {


    public static void main(String[] args) {
        int[] temperatures={73,74,75,71,69,72,76,73};
        int[] ints = dailyTemperatures(temperatures);
        for (int i = 0; i < ints.length; i++) {
            System.out.print(ints[i]+" ");
        }
    }
    public static int[] dailyTemperatures(int[] temperatures) {
        int[] ans=new int[temperatures.length];
        Deque<Integer> deque=new LinkedList<>();
        for (int i = 0; i < temperatures.length; i++) {
            while (!deque.isEmpty()&&temperatures[i]>temperatures[deque.peekFirst()] ){
                ans[deque.peekFirst()]=i-deque.peekFirst();
                deque.pollFirst();
            }
            deque.addLast(i);
        }
        return ans;
    }
}
