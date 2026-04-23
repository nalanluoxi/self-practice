package luogu;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @Author 纳兰洛熙
 * @Package：luogu
 * @Project：LanQiaoBei
 * @name：无重复字符的最长子串
 * @Date：2025/3/19 8:08
 * @Filename：无重复字符的最长子串
 */
public class 无重复字符的最长子串 {
    public static void main(String[] args) {
       // System.out.println(lengthOfLongestSubstring("bbbb"));
      //  System.out.println(lengthOfLongestSubstring("pwwkew"));
        System.out.println(lengthOfLongestSubstring("aab"));
    }

    public static int lengthOfLongestSubstring(String s) {
        Set<Character> visited=new HashSet<>();
        Deque<Character> deque=new LinkedList<>();
        int len = s.length();
        int ans=Integer.MIN_VALUE;
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (visited.contains(c)){
                ans=Math.max(ans,deque.size());
                while (!deque.isEmpty() && deque.peekFirst()!=c){
                    visited.remove(deque.pollFirst());
                }
                deque.pollFirst();
                deque.offerLast(c);
                visited.add(c);
            }else {
                visited.add(c);
                deque.offerLast(c);
            }
        }
        if (!deque.isEmpty()){
            ans=Math.max(ans,deque.size());
        }
        return ans==Integer.MIN_VALUE?deque.size():ans;
    }
}
