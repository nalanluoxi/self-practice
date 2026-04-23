package likou;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：用栈实现队列
 * @Date：2025/2/24 14:49
 * @Filename：用栈实现队列
 */
public class 用栈实现队列 {

    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        //System.out.println(list.poll());
        //System.out.println(list.poll());
        //System.out.println(list.poll());
        System.out.println(list.get(0));
        //System.out.println(list.poll());
        //System.out.println(list.get(0));
        System.out.println(list.pop());
        System.out.println(list.get(0));
    }
    class MyQueue {
        LinkedList<Integer> list;
        public MyQueue() {
            list=new LinkedList<>();
        }

        public void push(int x) {
            list.add(x);
        }

        public int pop() {
            return list.pop();
        }

        public int peek() {
            return list.get(0);
        }

        public boolean empty() {
            return list.isEmpty();
        }
    }
}
