package likou;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：有效的括号
 * @Date：2025/3/24 17:27
 * @Filename：有效的括号
 */
public class 有效的括号 {
    public static void main(String[] args) {
        // String s = "()[]{}";
        String s = "([])";
        System.out.println(isValid(s));
    }


    static HashMap<Character, Character> map = new HashMap<>();

    public static boolean isValid(String s) {

        map.put('(', ')');
        map.put('{', '}');
        map.put('[', ']');

        Deque<Character> deque = new LinkedList<>();
        for (int i = 0; i < s.length(); i++) {
            char temp = s.charAt(i);
            if (map.containsKey(temp)) {
                deque.offer(map.get(temp));
            }else {
                if (deque.isEmpty() ) {
                    return false;
                } else if (deque.peekLast() == temp) {
                    deque.pollLast();
                }else {
                    return false;
                }
            }
        }
        if (!deque.isEmpty()){
            return false;
        }
        return true;
    }
}
