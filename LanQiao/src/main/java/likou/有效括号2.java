package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：有效括号2
 * @Date：2025/2/25 19:27
 * @Filename：有效括号2
 */
public class 有效括号2 {
    public static void main(String[] args) {
       // System.out.println(left);
        System.out.println(isValid("([}}])"));
    }

   /* static char[] stack;
    static int r;
    static List<Character> left = new ArrayList<>();
    static List<Character> right = new ArrayList<>();

    public static boolean isValid(String s) {
        int len = s.length();
        if (len % 2 != 0||len==0) {
            return false;
        }
        left.add('(');
        left.add('[');
        left.add('{');
        right.add(')');
        right.add(']');
        right.add('}');

        stack = new char[len];
        r = 0;
        for (int i = 0; i < len; i++) {
            char temp = s.charAt(i);
            if (left.contains(temp)) {
                stack[r++] = temp;
            } else if (right.contains(temp)) {
                if (r == 0) {
                    return false;
                }
                if (stack[r - 1] == left.get(right.indexOf(temp))) {
                    r--;
                }else {
                    return false;
                }
            }
        }
        if (r!=0){
            return false;
        }
        return true;
    }*/

    static char[] stack;
    static int r;
    public static boolean isValid(String s) {
        int len = s.length();
        if (len % 2!= 0||len==0) {
            return false;
        }
        stack = new char[len];
        r = 0;
        List<Character> left = new ArrayList<>();
        List<Character> right = new ArrayList<>();
        left.add('(');
        left.add('[');
        left.add('{');
        right.add(')');
        right.add(']');
        right.add('}');
        for (int i = 0; i < len; i++) {
            char temp = s.charAt(i);
            if (left.contains(temp)) {
                stack[r++]=right.get(left.indexOf(temp));
            } else if (right.contains(temp)) {
                if (r==0||stack[r-1]!=temp){
                    return false;
                }
                r--;
            }
        }
        if (r!=0){
            return false;
        }
        return true;
    }
}
