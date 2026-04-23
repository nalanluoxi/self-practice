package likou;

import javax.management.Query;
import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：前k个高频元素II
 * @Date：2025/7/11 11:20
 * @Filename：前k个高频元素II
 */
public class 前k个高频元素II {
    public static void main(String[] args) {
        int[] ints1 = topKFrequent(new int[]{ 2, 2, 3,1,1,1}, 2);
        int[] ints2 = topKFrequent(new int[]{-1,-1}, 1);
        for (int i : ints1) {
            System.out.println(i);
        }
    }

    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer,Integer> map = new HashMap<>();
        for (int num : nums) {
            if (map.containsKey(num)){
                map.put(num,map.get(num)+1);
            }else {
                map.put(num,1);
            }
        }
        Queue<int[]> queue=new PriorityQueue<>((a,b)->{
            return a[1]-b[1];
        });
        for (Integer i : map.keySet()) {
            int[]temp=new int[]{i,map.get(i)};
            queue.add(temp);
            if (queue.size()>k){
                queue.poll();
            }
        }
        int[]ans=new int[k];
        for (int i = 0; i < k; i++) {
           ans[i]=queue.poll()[0];
        }
        return ans;
    }
}
