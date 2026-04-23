package likou.单调栈;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @Author 纳兰洛熙
 * @Package：likou.单调栈
 * @Project：LanQiaoBei
 * @name：无重复字符的最长子串
 * @Date：2025/6/14 11:38
 * @Filename：无重复字符的最长子串
 */
public class 无重复字符的最长子串 {
    public static void main(String[] args) {
        //String s="abcabcbb";
        String s=" ";
        int i = lengthOfLongestSubstring(s);
        System.out.println(i);
    }
    public static int lengthOfLongestSubstring(String s) {
        Set<Character> set=new HashSet<>();
        Deque<Integer> deque=new LinkedList<>();
        int len=s.length();
        int maxlen=0;
        for(int i=0;i<len;i++){
            char c=s.charAt(i);
            while(!deque.isEmpty()&&set.contains(c)){
                int l=deque.size();
                int st=deque.pollFirst();
                set.remove(s.charAt(st));
                maxlen=Math.max(maxlen,l);
            }
            deque.addLast(i);
            set.add(c);
        }
        if (!deque.isEmpty()){
            maxlen=Math.max(maxlen,deque.size());
        }
        return maxlen;
    }

}
