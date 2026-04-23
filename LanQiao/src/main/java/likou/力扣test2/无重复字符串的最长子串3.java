package likou.力扣test2;

import likou.二的幂;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：无重复字符串的最长子串3
 * @Date：2025/7/15 9:27
 * @Filename：无重复字符串的最长子串3
 */
public class 无重复字符串的最长子串3 {
    public static void main(String[] args) {
        //System.out.println(lengthOfLongestSubstring("abcabcbb"));
        System.out.println(lengthOfLongestSubstring("pwwkew"));
    }

    public static int lengthOfLongestSubstring(String s) {
        Deque<Character> deque=new LinkedList<>();
        Set<Character> set = new HashSet<>();
        int ans=Integer.MIN_VALUE;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            while (!deque.isEmpty() && set.contains(c)){
                int size = deque.size();
                ans=Math.max(ans,size);
                Character poll = deque.pollFirst();
                set.remove(poll);
            }
            deque.addLast(c);
            set.add(c);
            //set[c-'a']=1;
        }
        if (!deque.isEmpty()){
            ans=Math.max(ans,deque.size());
        }
        return ans==Integer.MIN_VALUE?0:ans;
    }
}
