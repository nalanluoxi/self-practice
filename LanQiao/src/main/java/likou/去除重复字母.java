package likou;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：去除重复字母
 * @Date：2025/2/25 16:48
 * @Filename：去除重复字母
 */
public class 去除重复字母 {
    public static void main(String[] args) {

        System.out.println(removeDuplicateLetters("cbacdcbc"));
    }


    //static StringBuilder str;
    static int[] hash;
    static char[] stack;

    static boolean[] isvisited;
    static int r;

    public static String removeDuplicateLetters(String s) {
        int len = s.length();
        char[] list = s.toCharArray();
        hash = new int[26];
        isvisited = new boolean[26];
        Arrays.fill(isvisited, false);
        stack = new char[26];
        r = 0;
        //  str = new StringBuilder();
        for (int i = 0; i < list.length; i++) {
            hash[list[i] - 'a']++;
        }
        for (int i = 0; i < len; i++) {
            char temp = list[i];
            if (!isvisited[temp - 'a']) {
                while (r > 0 && temp < stack[r - 1] && hash[stack[r - 1] - 'a'] > 0) {
                    isvisited[stack[--r] - 'a'] = false;
                }
                stack[r++] = temp;
                isvisited[temp - 'a'] = true;
            }
            hash[temp - 'a']--;
        }
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < r; i++) {
            str.append(stack[i]);
        }
        return str.toString();
    }

}
