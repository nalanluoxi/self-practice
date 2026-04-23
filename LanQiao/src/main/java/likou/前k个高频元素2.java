package likou;

import java.util.*;
import java.util.concurrent.DelayQueue;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：前k个高频元素2
 * @Date：2025/7/3 21:22
 * @Filename：前k个高频元素2
 */
public class 前k个高频元素2 {


    public static void main(String[] args) {
        int[]nums={1,1,1,2,2,3};
        int[] ints = topKFrequent(nums, 2);
    }

    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer,Integer> map = new HashMap<>();
        for (int num : nums) {
            if (!map.containsKey(num)){
                map.put(num,1);
            }else {
                map.put(num,map.get(num)+1);
            }
        }
        Queue<int[]> queue=new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0]-o2[0];
            }
        });
        for (Integer key : map.keySet()) {
            Integer value = map.get(key);
            if (queue.size()<k){
                queue.add(new int[]{value,key});
            }else {
                if (queue.peek()[0]<value){
                    queue.poll();
                    queue.add(new int[]{value,key});
                }
            }
        }
        int[]ans=new int[k];
        for (int i = 0; i < k; i++) {
            ans[i]=queue.poll()[1];
        }
        return ans;
    }
}
