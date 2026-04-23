package likou.力扣test2;

import ch.qos.logback.core.pattern.color.ANSIConstants;
import 设计模式.结构模式.代理模式.SellTickets;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：Test0723
 * @Date：2025/7/23 12:48
 * @Filename：Test0723
 */
public class Test0723 {
    public static void main(String[] args) {
        System.out.println(subarraySum(new int[]{1,1,1}, 2));
    }


    public static int subarraySum(int[] nums, int k) {
        int pre=0;
        int count=0;
        Map<Integer,Integer> map = new HashMap<>();
        map.put(0,1);
        for (int num : nums) {
            pre+=num;
            if (map.containsKey(pre-k)){
                count+=map.get(pre-k);
            }
            map.put(pre,map.getOrDefault(pre,0)+1);
        }
        return count;
    }

    public static int lengthOfLongestSubstring(String s) {
        Set<Character> set = new HashSet<>();
        Deque<Character> deque=new LinkedList<>();
        int ans=0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            while (set.contains(c)){
                ans=Math.max(ans,deque.size());
                Character polled = deque.pollFirst();
                set.remove(polled);
            }
            set.add(c);
            deque.addLast(c);
        }
        if (!deque.isEmpty()){
            ans=Math.max(deque.size(),ans);
        }
        return ans;
    }
}
