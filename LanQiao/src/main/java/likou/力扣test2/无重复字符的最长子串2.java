package likou.力扣test2;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：无重复字符的最长子串2
 * @Date：2025/6/6 8:20
 * @Filename：无重复字符的最长子串2
 */
public class 无重复字符的最长子串2 {

    public static void main(String[] args) {
        System.out.println(lengthOfLongestSubstring("abcabcbb"));
    }


    static int ans;
    public static int lengthOfLongestSubstring(String s) {
        ans=0;
        Deque<Integer> deque=new LinkedList<>();
        Set<Character> set = new HashSet<>();
        char[] array = s.toCharArray();
        for (int i = 0; i < array.length; i++) {
            while ( !deque.isEmpty() && set.contains(array[i])){
                int size = deque.size();
                ans=Math.max(ans,size);
                Integer index = deque.pollFirst();
                set.remove(array[index]);
            }
            deque.add(i);
            set.add(array[i]);
        }
        while (!deque.isEmpty()){
            ans=Math.max(ans,deque.size());
            Integer i = deque.pollFirst();
            set.remove(array[i]);
        }
        return ans;
    }
}
