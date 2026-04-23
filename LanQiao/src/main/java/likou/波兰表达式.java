package likou;

import java.util.Stack;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：波兰表达式
 * @Date：2025/2/25 20:35
 * @Filename：波兰表达式
 */
public class 波兰表达式 {
    public static void main(String[] args) {
        //System.out.println(1/13);
        System.out.println(evalRPN(new String[]{"4", "13", "5", "/", "+"}));
    }


    static Stack<Integer> stack;
    public static int evalRPN(String[] tokens) {
        stack = new Stack<>();
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (token.equals("+")) {
                stack.push(stack.pop() + stack.pop());
            }else if (token.equals("-")) {
                stack.push(-stack.pop() + stack.pop());
            }else if (token.equals("*")) {
                stack.push(stack.pop() * stack.pop());
            }else if (token.equals("/")) {
                Integer p2 = stack.pop();
                Integer p1 = stack.pop();
                stack.push(p1 / p2);
            } else {
                stack.push(Integer.parseInt(token));
            }
        }
        return stack.pop();
    }
}
