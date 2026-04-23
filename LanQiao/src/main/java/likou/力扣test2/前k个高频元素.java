package likou.力扣test2;

import javax.management.Query;
import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：前K
 * @Date：2025/7/13 11:57
 * @Filename：前K
 */
public class 前k个高频元素{
    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer,Integer> map = new TreeMap<>();
        for (int num : nums) {
            if (map.containsKey(num)){
                map.put(num,map.get(num)+1);
            }else {
                map.put(num,1);
            }
        }
        Queue<int[]> queue=new PriorityQueue<>((a,b)->a[1]-b[1]);
        for (Integer i : map.keySet()) {
            if (queue.size()!=k){
                queue.add(new int[]{i,map.get(i)});
            }else {
                queue.add(new int[]{i,map.get(i)});
                queue.poll();
            }
        }
        int []ans=new int[k];
        for (int i = 0; i < k; i++) {
            ans[i]=queue.poll()[0];
        }
        return ans;
    }
}
