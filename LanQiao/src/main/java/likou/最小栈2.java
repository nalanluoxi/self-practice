package likou;

import java.util.Stack;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：最小栈2
 * @Date：2025/5/3 20:39
 * @Filename：最小栈2
 */
public class 最小栈2 {
    class MinStack {

        Stack<Integer> data;
        Stack<Integer> min;
        public MinStack() {
            data = new Stack<>();
            min = new Stack<>();
        }

        public void push(int val) {
            data.push(val);
            if (min.isEmpty()){
                min.push(val);
            }else {
                min.push(Math.min(val,min.peek()));
            }
        }

        public void pop() {
            data.pop();
            min.pop();
        }

        public int top() {
            return data.peek();
        }

        public int getMin() {
            return min.peek();
        }
    }
}
