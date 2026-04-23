package likou;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：有效括号
 * @Date：2025/1/25 10:03
 * @Filename：有效括号
 */
public class 有效括号 {
    public static void main(String[] args) {

    }

    static Map<Character,Character> map;
    {
        map = new LinkedHashMap<>();
        map.put(')','(');
        map.put(']','[');
        map.put('}','{');
    }
    public static  boolean isValid(String s) {
        if (s.length()%2==1){
            return false;
        }
        Deque<Character> list=new LinkedList<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (map.containsKey(c)){
                if (list.peek()!=map.get(c)){
                    return false;
                }
                list.pop();
            }else {
                list.push(c);
            }
        }

        return list.isEmpty();
    }
}
