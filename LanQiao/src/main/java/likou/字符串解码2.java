package likou;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：字符串解码2
 * @Date：2025/5/4 21:37
 * @Filename：字符串解码2
 */
public class 字符串解码2 {
    public static void main(String[] args) {
        String s = "3[a2[c]]";
        System.out.println(decodeString(s));
    }

    public static String decodeString(String s) {
        LinkedList<String> stack = new LinkedList<>();
        int length = s.length();
        int i=0;
        while (i < length) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                String num="";
                while (Character.isDigit(s.charAt(i))){
                    num+=s.charAt(i);
                    i++;
                }
                stack.addLast(num);
            } else if (Character.isLetter(c) || c == '[') {
                stack.addLast(String.valueOf(c));
                i++;
            } else {
                i++;
                LinkedList<String> sub = new LinkedList<>();
                while (!"[".equals(stack.peekLast())) {
                    sub.addLast(stack.removeLast());
                }
                Collections.reverse(sub);
                String string = getString(sub);
                stack.removeLast();
                int repTime = Integer.parseInt(stack.removeLast());
                StringBuffer t = new StringBuffer();
                while (repTime > 0) {
                    t.append(string);
                    repTime--;
                }
                stack.addLast(t.toString());
            }
        }
        return getString(stack);
    }

    private static String getString(LinkedList<String> sub) {
        StringBuffer sb = new StringBuffer();
        for (String s : sub) {
            sb.append(s);
        }
        return sb.toString();
    }


}
