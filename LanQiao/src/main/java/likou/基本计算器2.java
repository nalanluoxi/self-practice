package likou;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：基本计算器2
 * @Date：2025/5/14 16:39
 * @Filename：基本计算器2
 */
public class 基本计算器2 {
    public static void main(String[] args) {
       // String s = "3+2*2";
        String s = " 3/2 ";

        System.out.println(calculate(s));
    }

    static int ans;
    static Deque<Integer> stack;

    public static int calculate(String s) {
        s=s.replaceAll(" ","");
        ans = 0;
        stack = new LinkedList<>();
        Character way = '+';
        int num = 0;
        for (int i = 0; i <= s.length(); i++) {
            if (i < s.length() && Character.isDigit(s.charAt(i))) {
                num = num * 10 + s.charAt(i) - '0';
            } else if (way == '+') {
                stack.offerLast(num);
                num = 0;
            } else if (way == '-') {
                stack.offerLast(-num);
                num = 0;
            } else if (way == '*') {
                stack.offerLast(stack.pollLast() * num);
                num = 0;
            } else if (way == '/') {
                stack.offerLast(stack.pollLast() / num);
                num = 0;
            }
            if (i < s.length() && !Character.isDigit(s.charAt(i))) {
                way = s.charAt(i);
            }
        }
        while (!stack.isEmpty()) {
            ans += stack.pollLast();
        }
        return ans;
    }

    public static int calculate1(String s) {
        Deque<Integer> stack = new ArrayDeque<Integer>();
        char preSign = '+';
        int num = 0;
        int n = s.length();
        for (int i = 0; i < n; ++i) {
            if (Character.isDigit(s.charAt(i))) {
                num = num * 10 + s.charAt(i) - '0';
            }
            if (!Character.isDigit(s.charAt(i)) && s.charAt(i) != ' ' || i == n - 1) {
                switch (preSign) {
                    case '+':
                        stack.push(num);
                        break;
                    case '-':
                        stack.push(-num);
                        break;
                    case '*':
                        stack.push(stack.pop() * num);
                        break;
                    default:
                        stack.push(stack.pop() / num);
                }
                preSign = s.charAt(i);
                num = 0;
            }
        }
        int ans = 0;
        while (!stack.isEmpty()) {
            ans += stack.pop();
        }
        return ans;
    }

}
