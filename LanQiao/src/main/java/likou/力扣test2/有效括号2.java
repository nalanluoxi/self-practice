package likou.力扣test2;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：有效括号2
 * @Date：2025/6/28 12:05
 * @Filename：有效括号2
 */
public class 有效括号2 {

    public static void main(String[] args) {
        System.out.println(isValid("()"));
        System.out.println(isValid("()[]{}"));
        System.out.println(isValid("(]"));
        System.out.println(isValid("([)]"));
        System.out.println(isValid("{[]}"));
    }
    public static boolean isValid(String s) {
        char[] list = s.toCharArray();
        Map<Character,Character> map = new HashMap<>();
        map.put('(',')');
        map.put('[',']');
        map.put('{','}');
        Deque<Character> deque=new LinkedList<>();
        for (char c : list) {
            if (map.containsKey(c)){
                deque.addLast(map.get(c));
            }else {
                if (deque.isEmpty()||deque.peekLast()!=c){
                    return false;
                }else {
                    deque.pollLast();
                }
            }
        }
        if (!deque.isEmpty()){
            return false;
        }
        return true;
    }
}
