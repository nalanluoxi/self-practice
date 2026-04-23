package likou.力扣test2;

import likou.动态规划.不同的二叉搜索树;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：无重复字符的最长子串3
 * @Date：2025/6/26 17:26
 * @Filename：无重复字符的最长子串3
 */
public class 无重复字符的最长子串3 {
    public static void main(String[] args) {

    }


    static Set<Character> set;
    static Deque<Character> queue;
    public static int lengthOfLongestSubstring(String s) {
        set=new HashSet<>();
        queue=new LinkedList<>();
        int ans=0;
        for (int i = 0; i < s.length(); i++) {
            while (!queue.isEmpty() && set.contains(s.charAt(i))){
                ans=Math.max(ans,queue.size());
                Character c = queue.pollFirst();
                set.remove(c);
            }
            set.add(s.charAt(i));
            queue.addLast(s.charAt(i));
        }
        while (queue.size()!=0){
            ans=Math.max(ans,queue.size());
            Character c = queue.pollFirst();
            set.remove(c);
        }
        return ans;
    }
}
