package likou.力扣test2;

import java.awt.image.ImageProducer;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：有效括号
 * @Date：2025/6/4 19:33
 * @Filename：有效括号
 */
public class 有效括号 {
    public static void main(String[] args) {

    }


    static Map<Character,Character> map=new HashMap<>();
    static{
        map.put('(', ')');
        map.put('{', '}');
        map.put('[', ']');
    }
    public static boolean isValid(String s) {
        int n = s.length();
        if (n % 2 == 1) {
            return false;
        }
        Deque<Character> deque=new LinkedList<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (map.containsKey(c)){
                deque.offerLast(map.get(c));
            }else {
                if (deque.isEmpty() || deque.peekLast()!=c){
                    return false;
                }else {
                    deque.pollLast();
                }
            }
        }
        return deque.isEmpty();
    }
}
