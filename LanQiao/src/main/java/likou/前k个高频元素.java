package likou;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：前k个高频元素
 * @Date：2025/3/9 11:55
 * @Filename：前k个高频元素
 */
public class 前k个高频元素 {
    public static void main(String[] args) {
        int[] ints = topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }


   static PriorityQueue<int[]> queue;
    static int[]ans;
    static Map<Integer,Integer> hash;
    public static int[] topKFrequent(int[] nums, int k) {
        hash=new HashMap<>();
        for (int num : nums) {
            if (hash.containsKey(num)){
                hash.put(num,hash.get(num)+1);
            }else {
                hash.put(num,1);
            }
        }
        ans=new int[k];
        queue=new PriorityQueue<>(
                (p1,p2)->p2[1]-p1[1]
        );
        for (Map.Entry<Integer, Integer> entry : hash.entrySet()) {
            queue.add(new int[]{entry.getKey(),entry.getValue()});
        }
        for (int i = 0; i < k; i++) {
            ans[i]=queue.poll()[0];
        }
       /* // 将 HashMap 的键值对转换为 List
        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(hash.entrySet());

        // 使用 Collections.sort 方法结合自定义的 Comparator 进行排序
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        ans = new int[k];
        for (int i = 0; i < k; i++) {
            ans[i] = list.get(i).getKey();
        }
*/
        return ans;


    }
}
