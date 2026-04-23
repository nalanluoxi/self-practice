package likou.力扣test2;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;


/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：唔重复字符的最长子串
 * @Date：2025/5/20 23:03
 * @Filename：唔重复字符的最长子串
 */
public class 无重复字符的最长子串 {
    public static void main(String[] args) {
        System.out.println(lengthOfLongestSubstring("abcabcbb"));
        System.out.println(lengthOfLongestSubstring(" "));
    }


    static int ans;
    static Deque<Character> deque;
  //  static int [] words;
    static Set<Character> set;
    public static int lengthOfLongestSubstring(String s) {
        if (s.length()==0||s==""){
            return 0;
        }
        ans=0;
        deque=new LinkedList<>();
    set=new HashSet<>();
        //    words=new int[27];
        for (int i = 0; i < s.length(); i++) {
            char temp = s.charAt(i);
            while (!deque.isEmpty()&&set.contains(temp)){
                Character c = deque.pollFirst();
                set.remove(c);
            }
            deque.offerLast(temp);
            set.add(temp);
            ans= Math.max(ans,deque.size());
        }
        return ans;
    }
}
