package likou;

import java.util.Stack;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：删除相邻重复
 * @Date：2025/2/25 19:52
 * @Filename：删除相邻重复
 */
public class 删除相邻重复 {
    public static void main(String[] args) {
        System.out.println(removeDuplicates("abbaca"));
    }

    static Stack<Character> stack;
    public static String removeDuplicates(String s) {
        if (s.length()==0){
            return "";
        }
        stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char temp = s.charAt(i);
            if (!stack.isEmpty() && stack.peek() != temp) {
                stack.push(temp);
            }else if (stack.isEmpty()){
                stack.push(temp);
            }else {
                stack.pop();
            }
        }
        StringBuilder str = new StringBuilder();
        while (!stack.isEmpty()){
            str.append(stack.pop());
        }
        return str.reverse().toString();
    }
}
