package likou;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：数据的中位数
 * @Date：2025/7/13 9:56
 * @Filename：数据的中位数
 */
public class 数据的中位数 {

    public static void main(String[] args) {
        MedianFinder t=new MedianFinder();
        t.addNum(1);
        t.addNum(2);
        System.out.println(t.findMedian());
        t.addNum(3);
        System.out.println(t.findMedian());
    }static class MedianFinder {

        public Queue<Integer> minQueue;
        public Queue<Integer>
     maxQueue;
        public MedianFinder() {
            //大到小 小的一半
            maxQueue=new PriorityQueue<>((a,b)->b-a);
            //小到大 大的一半
            minQueue=new PriorityQueue<>((a,b)->a-b);
        }

        public void addNum(int num) {
           if (minQueue.size()!=maxQueue.size()){
                minQueue.add(num);
                maxQueue.add(minQueue.poll());
           }else {
               maxQueue.add(num);
               minQueue.add(maxQueue.poll());
           }
        }

        public double findMedian() {
           return minQueue.size()==maxQueue.size()?(minQueue.peek()+maxQueue.peek())/2.0:minQueue.peek();
        }
    }
}
