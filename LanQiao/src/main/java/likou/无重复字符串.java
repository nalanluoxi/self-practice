package likou;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：无重复字符串
 * @Date：2025/1/22 14:35
 * @Filename：无重复字符串
 */
public class 无重复字符串 {
    public static void main(String[] args) {
        String s = "abcabcbb";
        System.out.println(lengthOfLongestSubstring(s));
    }

    static Map<Character, Integer> list;

    public static int lengthOfLongestSubstring(String s) {
        if (s.length() <= 1) {
            return s.length();
        }
        int maxlen = 1;
        Set<Character> visited = new HashSet<>();
        int len = s.length();
        int l = 0;
        visited.add(s.charAt(l));
        for (int r = 1; r < len;) {
            if (!visited.contains(s.charAt(r))) {
                visited.add(s.charAt(r));
                r++;
            } else {

                visited.remove(s.charAt(l));
                l++;
            }
            maxlen = Math.max(maxlen, r - l);
        }
        return maxlen;
    }

}
